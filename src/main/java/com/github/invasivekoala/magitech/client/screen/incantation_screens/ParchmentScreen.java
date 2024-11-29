package com.github.invasivekoala.magitech.client.screen.incantation_screens;

import com.github.invasivekoala.magitech.Magitech;
import com.github.invasivekoala.magitech.packets.PacketRegistry;
import com.github.invasivekoala.magitech.packets.ServerboundEditParchmentPacket;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class ParchmentScreen extends Screen {


    private static final ResourceLocation PARCHMENT_SCREEN = new ResourceLocation(Magitech.MOD_ID, "textures/gui/parchment.png");
    private static final Component EDIT_TITLE_LABEL = new TranslatableComponent("book.editTitle");
    private static final Component FINALIZE_WARNING_LABEL = new TranslatableComponent("book.finalizeWarning");
    private static final FormattedCharSequence BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
    private static final FormattedCharSequence GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));
    private final Player owner;
    private final ItemStack book;
    /** Whether the book's title or contents has been modified since being opened */
    private boolean isModified;
    /** Determines if the signing screen is open */
    private boolean isSigning;
    /** Update ticks since the gui was opened */
    private int frameTick;
    private int currentPage;
    private final List<String> pages = Lists.newArrayList();
    private String title = "";
    private final TextFieldHelper pageEdit = new TextFieldHelper(this::getCurrentPageText, this::setCurrentPageText, this::getClipboard, this::setClipboard, (p_98179_) -> {
        return p_98179_.length() < 1024 && this.font.wordWrapHeight(p_98179_, 114) <= 128;
    });
    private final TextFieldHelper titleEdit = new TextFieldHelper(() -> {
        return this.title;
    }, (p_98175_) -> {
        this.title = p_98175_;
    }, this::getClipboard, this::setClipboard, (p_98170_) -> {
        return p_98170_.length() < 16;
    });
    /** In milliseconds */
    private long lastClickTime;
    private int lastIndex = -1;
    private PageButton forwardButton;
    private PageButton backButton;
    private Button doneButton;
    private Button signButton;
    private Button finalizeButton;
    private Button cancelButton;
    private final InteractionHand hand;
    @Nullable
    private ParchmentScreen.DisplayCache displayCache = ParchmentScreen.DisplayCache.EMPTY;
    private Component pageMsg = TextComponent.EMPTY;
    private final Component ownerText;

    public ParchmentScreen(Player pOwner, ItemStack pBook, InteractionHand pHand) {
        super(NarratorChatListener.NO_TITLE);
        this.owner = pOwner;
        this.book = pBook;
        this.hand = pHand;
        CompoundTag compoundtag = pBook.getTag();
        if (compoundtag != null) {
            this.pages.add(compoundtag.getString("rawText"));
        }

        if (this.pages.isEmpty()) {
            this.pages.add("");
        }

        this.ownerText = (new TranslatableComponent("book.byAuthor", pOwner.getName())).withStyle(ChatFormatting.DARK_GRAY);
    }

    private void setClipboard(String p_98148_) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, p_98148_);
        }

    }

    private String getClipboard() {
        return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }

    /**
     * Returns the number of pages in the book
     */
    private int getNumPages() {
        return this.pages.size();
    }

    public void tick() {
        super.tick();
        ++this.frameTick;
    }

    protected void init() {
        this.clearDisplayCache();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.signButton = this.addRenderableWidget(new Button(this.width / 2 - 100, 196, 98, 20, new TranslatableComponent("book.signButton"), (p_98177_) -> {
            this.isSigning = true;
            this.updateButtonVisibility();
        }));
        this.doneButton = this.addRenderableWidget(new Button(this.width / 2 + 2, 196, 98, 20, CommonComponents.GUI_DONE, (p_98173_) -> {
            this.minecraft.setScreen(null);
            this.saveChanges(false);
        }));
        this.finalizeButton = this.addRenderableWidget(new Button(this.width / 2 - 100, 196, 98, 20, new TranslatableComponent("book.finalizeButton"), (p_98168_) -> {
            if (this.isSigning) {
                this.saveChanges(true);
                this.minecraft.setScreen(null);
            }

        }));
        this.cancelButton = this.addRenderableWidget(new Button(this.width / 2 + 2, 196, 98, 20, CommonComponents.GUI_CANCEL, (p_98157_) -> {
            if (this.isSigning) {
                this.isSigning = false;
            }

            this.updateButtonVisibility();
        }));
        int i = (this.width - 192) / 2;
        int j = 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 159, true, (p_98144_) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PageButton(i + 43, 159, false, (p_98113_) -> {
            this.pageBack();
        }, true));
        this.updateButtonVisibility();
    }

    /**
     * Displays the previous page
     */
    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }

    /**
     * Displays the next page (creating it if necessary)
     */
    private void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        } else {
            this.appendPageToBook();
            if (this.currentPage < this.getNumPages() - 1) {
                ++this.currentPage;
            }
        }

        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }

    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    /**
     * Sets visibility for book buttons
     */
    private void updateButtonVisibility() {
        this.backButton.visible = !this.isSigning && this.currentPage > 0;
        this.forwardButton.visible = !this.isSigning;
        this.doneButton.visible = !this.isSigning;
        this.signButton.visible = !this.isSigning;
        this.cancelButton.visible = this.isSigning;
        this.finalizeButton.visible = this.isSigning;
        this.finalizeButton.active = !this.title.trim().isEmpty();
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> listiterator = this.pages.listIterator(this.pages.size());

        while(listiterator.hasPrevious() && listiterator.previous().isEmpty()) {
            listiterator.remove();
        }

    }

    private void saveChanges(boolean pPublish) {
        if (this.isModified) {
            this.eraseEmptyTrailingPages();
            this.updateLocalCopy(pPublish);
            int i = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().selected : 40;
            PacketRegistry.sendToServer(new ServerboundEditParchmentPacket(pages.get(0), i));
        }
    }

    private void updateLocalCopy(boolean pSign) {
        ListTag listtag = new ListTag();
        this.pages.stream().map(StringTag::valueOf).forEach(listtag::add);
        if (!this.pages.isEmpty()) {
            this.book.addTagElement("rawText", StringTag.valueOf(pages.get(0)));
        }


    }

    /**
     * Adds a new page to the book (capped at 100 pages)
     */
    private void appendPageToBook() {
        if (this.getNumPages() < 100) {
            this.pages.add("");
            this.isModified = true;
        }
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (this.isSigning) {
            return this.titleKeyPressed(pKeyCode, pScanCode, pModifiers);
        } else {
            boolean flag = this.bookKeyPressed(pKeyCode, pScanCode, pModifiers);
            if (flag) {
                this.clearDisplayCache();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (super.charTyped(pCodePoint, pModifiers)) {
            return true;
        } else if (this.isSigning) {
            boolean flag = this.titleEdit.charTyped(pCodePoint);
            if (flag) {
                this.updateButtonVisibility();
                this.isModified = true;
                return true;
            } else {
                return false;
            }
        } else if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
            this.pageEdit.insertText(Character.toString(pCodePoint));
            this.clearDisplayCache();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handles keypresses, clipboard functions, and page turning
     */
    private boolean bookKeyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (Screen.isSelectAll(pKeyCode)) {
            this.pageEdit.selectAll();
            return true;
        } else if (Screen.isCopy(pKeyCode)) {
            this.pageEdit.copy();
            return true;
        } else if (Screen.isPaste(pKeyCode)) {
            this.pageEdit.paste();
            return true;
        } else if (Screen.isCut(pKeyCode)) {
            this.pageEdit.cut();
            return true;
        } else {
            switch(pKeyCode) {
                case 257:
                case 335:
                    this.pageEdit.insertText("\n");
                    return true;
                case 259:
                    this.pageEdit.removeCharsFromCursor(-1);
                    return true;
                case 261:
                    this.pageEdit.removeCharsFromCursor(1);
                    return true;
                case 262:
                    this.pageEdit.moveByChars(1, Screen.hasShiftDown());
                    return true;
                case 263:
                    this.pageEdit.moveByChars(-1, Screen.hasShiftDown());
                    return true;
                case 264:
                    this.keyDown();
                    return true;
                case 265:
                    this.keyUp();
                    return true;
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                case 268:
                    this.keyHome();
                    return true;
                case 269:
                    this.keyEnd();
                    return true;
                default:
                    return false;
            }
        }
    }

    private void keyUp() {
        this.changeLine(-1);
    }

    private void keyDown() {
        this.changeLine(1);
    }

    private void changeLine(int pYChange) {
        int i = this.pageEdit.getCursorPos();
        int j = this.getDisplayCache().changeLine(i, pYChange);
        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }

    private void keyHome() {
        int i = this.pageEdit.getCursorPos();
        int j = this.getDisplayCache().findLineStart(i);
        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }

    private void keyEnd() {
        ParchmentScreen.DisplayCache ParchmentScreen$displaycache = this.getDisplayCache();
        int i = this.pageEdit.getCursorPos();
        int j = ParchmentScreen$displaycache.findLineEnd(i);
        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
    }

    /**
     * Handles special keys pressed while editing the book's title
     */
    private boolean titleKeyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        switch(pKeyCode) {
            case 257:
            case 335:
                if (!this.title.isEmpty()) {
                    this.saveChanges(true);
                    this.minecraft.setScreen((Screen)null);
                }

                return true;
            case 259:
                this.titleEdit.removeCharsFromCursor(-1);
                this.updateButtonVisibility();
                this.isModified = true;
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the contents of the current page as a string (or an empty string if the currPage isn't a valid page index)
     */
    private String getCurrentPageText() {
        return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(this.currentPage) : "";
    }

    private void setCurrentPageText(String p_98159_) {
        if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
            this.pages.set(this.currentPage, p_98159_);
            this.isModified = true;
            this.clearDisplayCache();
        }

    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        this.setFocused((GuiEventListener)null);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PARCHMENT_SCREEN);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.blit(pPoseStack, i, 2, 0, 0, 192, 192);

        int j1 = this.font.width(this.pageMsg);
        this.font.draw(pPoseStack, this.pageMsg, (float)(i - j1 + 192 - 44), 18.0F, 0);
        ParchmentScreen.DisplayCache ParchmentScreen$displaycache = this.getDisplayCache();

        for(ParchmentScreen.LineInfo ParchmentScreen$lineinfo : ParchmentScreen$displaycache.lines) {
            this.font.draw(pPoseStack, ParchmentScreen$lineinfo.asComponent, (float)ParchmentScreen$lineinfo.x, (float)ParchmentScreen$lineinfo.y, -16777216);
        }

        this.renderHighlight(ParchmentScreen$displaycache.selection);
        this.renderCursor(pPoseStack, ParchmentScreen$displaycache.cursor, ParchmentScreen$displaycache.cursorAtEnd);


        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderCursor(PoseStack pPoseStack, ParchmentScreen.Pos2i pCursorPos, boolean pIsEndOfText) {
        if (this.frameTick / 6 % 2 == 0) {
            pCursorPos = this.convertLocalToScreen(pCursorPos);
            if (!pIsEndOfText) {
                GuiComponent.fill(pPoseStack, pCursorPos.x, pCursorPos.y - 1, pCursorPos.x + 1, pCursorPos.y + 9, -16777216);
            } else {
                this.font.draw(pPoseStack, "_", (float)pCursorPos.x, (float)pCursorPos.y, 0);
            }
        }

    }

    private void renderHighlight(Rect2i[] pSelected) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.0F, 0.0F, 255.0F, 255.0F);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for(Rect2i rect2i : pSelected) {
            int i = rect2i.getX();
            int j = rect2i.getY();
            int k = i + rect2i.getWidth();
            int l = j + rect2i.getHeight();
            bufferbuilder.vertex((double)i, (double)l, 0.0D).endVertex();
            bufferbuilder.vertex((double)k, (double)l, 0.0D).endVertex();
            bufferbuilder.vertex((double)k, (double)j, 0.0D).endVertex();
            bufferbuilder.vertex((double)i, (double)j, 0.0D).endVertex();
        }

        tesselator.end();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    private ParchmentScreen.Pos2i convertScreenToLocal(ParchmentScreen.Pos2i pScreenPos) {
        return new ParchmentScreen.Pos2i(pScreenPos.x - (this.width - 192) / 2 - 36, pScreenPos.y - 32);
    }

    private ParchmentScreen.Pos2i convertLocalToScreen(ParchmentScreen.Pos2i pLocalScreenPos) {
        return new ParchmentScreen.Pos2i(pLocalScreenPos.x + (this.width - 192) / 2 + 36, pLocalScreenPos.y + 32);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
            return true;
        } else {
            if (pButton == 0) {
                long i = Util.getMillis();
                ParchmentScreen.DisplayCache ParchmentScreen$displaycache = this.getDisplayCache();
                int j = ParchmentScreen$displaycache.getIndexAtPosition(this.font, this.convertScreenToLocal(new ParchmentScreen.Pos2i((int)pMouseX, (int)pMouseY)));
                if (j >= 0) {
                    if (j == this.lastIndex && i - this.lastClickTime < 250L) {
                        if (!this.pageEdit.isSelecting()) {
                            this.selectWord(j);
                        } else {
                            this.pageEdit.selectAll();
                        }
                    } else {
                        this.pageEdit.setCursorPos(j, Screen.hasShiftDown());
                    }

                    this.clearDisplayCache();
                }

                this.lastIndex = j;
                this.lastClickTime = i;
            }

            return true;
        }
    }

    private void selectWord(int pIndex) {
        String s = this.getCurrentPageText();
        this.pageEdit.setSelectionRange(StringSplitter.getWordPosition(s, -1, pIndex, false), StringSplitter.getWordPosition(s, 1, pIndex, false));
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
            return true;
        } else {
            if (pButton == 0) {
                ParchmentScreen.DisplayCache ParchmentScreen$displaycache = this.getDisplayCache();
                int i = ParchmentScreen$displaycache.getIndexAtPosition(this.font, this.convertScreenToLocal(new ParchmentScreen.Pos2i((int)pMouseX, (int)pMouseY)));
                this.pageEdit.setCursorPos(i, true);
                this.clearDisplayCache();
            }

            return true;
        }
    }

    private ParchmentScreen.DisplayCache getDisplayCache() {
        if (this.displayCache == null) {
            this.displayCache = this.rebuildDisplayCache();
            this.pageMsg = new TranslatableComponent("book.pageIndicator", this.currentPage + 1, this.getNumPages());
        }

        return this.displayCache;
    }

    private void clearDisplayCache() {
        this.displayCache = null;
    }

    private void clearDisplayCacheAfterPageChange() {
        this.pageEdit.setCursorToEnd();
        this.clearDisplayCache();
    }

    private ParchmentScreen.DisplayCache rebuildDisplayCache() {
        String s = this.getCurrentPageText();
        if (s.isEmpty()) {
            return ParchmentScreen.DisplayCache.EMPTY;
        } else {
            int i = this.pageEdit.getCursorPos();
            int j = this.pageEdit.getSelectionPos();
            IntList intlist = new IntArrayList();
            List<ParchmentScreen.LineInfo> list = Lists.newArrayList();
            MutableInt mutableint = new MutableInt();
            MutableBoolean mutableboolean = new MutableBoolean();
            StringSplitter stringsplitter = this.font.getSplitter();
            stringsplitter.splitLines(s, 114, Style.EMPTY, true, (p_98132_, p_98133_, p_98134_) -> {
                int k3 = mutableint.getAndIncrement();
                String s2 = s.substring(p_98133_, p_98134_);
                mutableboolean.setValue(s2.endsWith("\n"));
                String s3 = StringUtils.stripEnd(s2, " \n");
                int l3 = k3 * 9;
                ParchmentScreen.Pos2i ParchmentScreen$pos2i1 = this.convertLocalToScreen(new ParchmentScreen.Pos2i(0, l3));
                intlist.add(p_98133_);
                list.add(new ParchmentScreen.LineInfo(p_98132_, s3, ParchmentScreen$pos2i1.x, ParchmentScreen$pos2i1.y));
            });
            int[] aint = intlist.toIntArray();
            boolean flag = i == s.length();
            ParchmentScreen.Pos2i ParchmentScreen$pos2i;
            if (flag && mutableboolean.isTrue()) {
                ParchmentScreen$pos2i = new ParchmentScreen.Pos2i(0, list.size() * 9);
            } else {
                int k = findLineFromPos(aint, i);
                int l = this.font.width(s.substring(aint[k], i));
                ParchmentScreen$pos2i = new ParchmentScreen.Pos2i(l, k * 9);
            }

            List<Rect2i> list1 = Lists.newArrayList();
            if (i != j) {
                int l2 = Math.min(i, j);
                int i1 = Math.max(i, j);
                int j1 = findLineFromPos(aint, l2);
                int k1 = findLineFromPos(aint, i1);
                if (j1 == k1) {
                    int l1 = j1 * 9;
                    int i2 = aint[j1];
                    list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i1, l1, i2));
                } else {
                    int i3 = j1 + 1 > aint.length ? s.length() : aint[j1 + 1];
                    list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i3, j1 * 9, aint[j1]));

                    for(int j3 = j1 + 1; j3 < k1; ++j3) {
                        int j2 = j3 * 9;
                        String s1 = s.substring(aint[j3], aint[j3 + 1]);
                        int k2 = (int)stringsplitter.stringWidth(s1);
                        list1.add(this.createSelection(new ParchmentScreen.Pos2i(0, j2), new ParchmentScreen.Pos2i(k2, j2 + 9)));
                    }

                    list1.add(this.createPartialLineSelection(s, stringsplitter, aint[k1], i1, k1 * 9, aint[k1]));
                }
            }

            return new ParchmentScreen.DisplayCache(s, ParchmentScreen$pos2i, flag, aint, list.toArray(new ParchmentScreen.LineInfo[0]), list1.toArray(new Rect2i[0]));
        }
    }

    static int findLineFromPos(int[] pLineStarts, int pFind) {
        int i = Arrays.binarySearch(pLineStarts, pFind);
        return i < 0 ? -(i + 2) : i;
    }

    private Rect2i createPartialLineSelection(String pInput, StringSplitter pSplitter, int p_98122_, int p_98123_, int p_98124_, int p_98125_) {
        String s = pInput.substring(p_98125_, p_98122_);
        String s1 = pInput.substring(p_98125_, p_98123_);
        ParchmentScreen.Pos2i ParchmentScreen$pos2i = new ParchmentScreen.Pos2i((int)pSplitter.stringWidth(s), p_98124_);
        ParchmentScreen.Pos2i ParchmentScreen$pos2i1 = new ParchmentScreen.Pos2i((int)pSplitter.stringWidth(s1), p_98124_ + 9);
        return this.createSelection(ParchmentScreen$pos2i, ParchmentScreen$pos2i1);
    }

    private Rect2i createSelection(ParchmentScreen.Pos2i pCorner1, ParchmentScreen.Pos2i pCorner2) {
        ParchmentScreen.Pos2i ParchmentScreen$pos2i = this.convertLocalToScreen(pCorner1);
        ParchmentScreen.Pos2i ParchmentScreen$pos2i1 = this.convertLocalToScreen(pCorner2);
        int i = Math.min(ParchmentScreen$pos2i.x, ParchmentScreen$pos2i1.x);
        int j = Math.max(ParchmentScreen$pos2i.x, ParchmentScreen$pos2i1.x);
        int k = Math.min(ParchmentScreen$pos2i.y, ParchmentScreen$pos2i1.y);
        int l = Math.max(ParchmentScreen$pos2i.y, ParchmentScreen$pos2i1.y);
        return new Rect2i(i, k, j - i, l - k);
    }
    
    static class DisplayCache {
        static final ParchmentScreen.DisplayCache EMPTY = new ParchmentScreen.DisplayCache("", new ParchmentScreen.Pos2i(0, 0), true, new int[]{0}, new ParchmentScreen.LineInfo[]{new ParchmentScreen.LineInfo(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
        private final String fullText;
        final ParchmentScreen.Pos2i cursor;
        final boolean cursorAtEnd;
        private final int[] lineStarts;
        final ParchmentScreen.LineInfo[] lines;
        final Rect2i[] selection;

        public DisplayCache(String pFullText, ParchmentScreen.Pos2i pCursor, boolean pCursorAtEnd, int[] pLineStarts, ParchmentScreen.LineInfo[] pLines, Rect2i[] pSelection) {
            this.fullText = pFullText;
            this.cursor = pCursor;
            this.cursorAtEnd = pCursorAtEnd;
            this.lineStarts = pLineStarts;
            this.lines = pLines;
            this.selection = pSelection;
        }

        public int getIndexAtPosition(Font pFont, ParchmentScreen.Pos2i pCursorPosition) {
            int i = pCursorPosition.y / 9;
            if (i < 0) {
                return 0;
            } else if (i >= this.lines.length) {
                return this.fullText.length();
            } else {
                ParchmentScreen.LineInfo ParchmentScreen$lineinfo = this.lines[i];
                return this.lineStarts[i] + pFont.getSplitter().plainIndexAtWidth(ParchmentScreen$lineinfo.contents, pCursorPosition.x, ParchmentScreen$lineinfo.style);
            }
        }

        public int changeLine(int pXChange, int pYChange) {
            int i = ParchmentScreen.findLineFromPos(this.lineStarts, pXChange);
            int j = i + pYChange;
            int k;
            if (0 <= j && j < this.lineStarts.length) {
                int l = pXChange - this.lineStarts[i];
                int i1 = this.lines[j].contents.length();
                k = this.lineStarts[j] + Math.min(l, i1);
            } else {
                k = pXChange;
            }

            return k;
        }

        public int findLineStart(int pLine) {
            int i = ParchmentScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[i];
        }

        public int findLineEnd(int pLine) {
            int i = ParchmentScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[i] + this.lines[i].contents.length();
        }
    }
    
    static class LineInfo {
        final Style style;
        final String contents;
        final Component asComponent;
        final int x;
        final int y;

        public LineInfo(Style pStyle, String pContents, int pX, int pY) {
            this.style = pStyle;
            this.contents = pContents;
            this.x = pX;
            this.y = pY;
            this.asComponent = (new TextComponent(pContents)).setStyle(pStyle);
        }
    }
    
    static class Pos2i {
        public final int x;
        public final int y;

        Pos2i(int pX, int pY) {
            this.x = pX;
            this.y = pY;
        }
    }
}
