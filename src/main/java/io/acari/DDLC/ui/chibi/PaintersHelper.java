package io.acari.DDLC.ui.chibi;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.AbstractPainter;
import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.openapi.ui.Painter;
import com.intellij.openapi.ui.Painter.Listener;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.Anchor;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil.Fill;
import com.intellij.util.ImageLoader;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.JBUI.ScaleContext;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

final class PaintersHelper implements Listener {
  static final RenderingHints.Key ADJUST_ALPHA = new RenderingHints.Key(1) {
    @Override
    public boolean isCompatibleValue(Object val) {
      return val instanceof Boolean;
    }
  };
  private static final Logger LOG = Logger.getInstance(PaintersHelper.class);
  private final Set<Painter> myPainters;
  private final Map<Painter, Component> myPainter2Component;
  private final JComponent myRootComponent;

  PaintersHelper(@NotNull JComponent component) {

    super();
    this.myPainters = ContainerUtil.newLinkedHashSet();
    this.myPainter2Component = ContainerUtil.newLinkedHashMap();
    this.myRootComponent = component;
  }

  public static void initWallpaperPainter(@NotNull String propertyName, @NotNull PaintersHelper painters) {


    PaintersHelper.ImagePainter painter = (PaintersHelper.ImagePainter) newWallpaperPainter(propertyName, painters.myRootComponent);
    painters.addPainter(painter, (Component) null);
  }

  private static AbstractPainter newWallpaperPainter(@NotNull final String propertyName, @NotNull final JComponent rootComponent) {


    return new PaintersHelper.ImagePainter() {
      Image image;
      float alpha;
      Insets insets;
      Fill fillType;
      Anchor anchor;
      String current;

      public boolean needsRepaint() {
        return this.ensureImageLoaded();
      }

      public void executePaint(Component component, Graphics2D g) {
        if (this.image != null) {
          this.executePaint(g, component, this.image, this.fillType, this.anchor, this.alpha, this.insets);
        }
      }

      boolean ensureImageLoaded() {
        IdeFrame frame = (IdeFrame) UIUtil.getParentOfType(IdeFrame.class, rootComponent);
        Project project = frame == null ? null : frame.getProject();
        String value = IdeBackgroundUtil.getBackgroundSpec(project, propertyName);
        if (!Comparing.equal(value, this.current)) {
          this.current = value;
          this.loadImageAsync(value);
        }

        return this.image != null;
      }

      private void resetImage(String value, Image newImage, float newAlpha, Fill newFill, Anchor newAnchor) {
        if (Comparing.equal(this.current, value)) {
          boolean prevOk = this.image != null;
          this.clearImages(-1L);
          this.image = newImage;
          this.insets = JBUI.emptyInsets();
          this.alpha = newAlpha;
          this.fillType = newFill;
          this.anchor = newAnchor;
          boolean newOk = newImage != null;
          if (prevOk || newOk) {
            ModalityState modalityState = ModalityState.stateForComponent(rootComponent);
            if (modalityState.dominates(ModalityState.NON_MODAL)) {
              UIUtil.getActiveWindow().repaint();
            } else {
              IdeBackgroundUtil.repaintAllWindows();
            }
          }

        }
      }

      private void loadImageAsync(@Nullable String propertyValue) {
        String[] parts = (propertyValue != null ? propertyValue : propertyName + ".png").split(",");
        float newAlpha = Math.abs(Math.min((float) StringUtil.parseInt(parts.length > 1 ? parts[1] : "", 10) / 100.0F, 1.0F));
        Fill newFillType = (Fill) StringUtil.parseEnum(parts.length > 2 ? parts[2].toUpperCase(Locale.ENGLISH) : "", Fill.SCALE, Fill.class);
        Anchor newAnchor = (Anchor) StringUtil.parseEnum(parts.length > 3 ? parts[3].toUpperCase(Locale.ENGLISH) : "", Anchor.CENTER, Anchor.class);
        String flip = parts.length > 4 ? parts[4] : "none";
        boolean flipH = "flipHV".equals(flip) || "flipH".equals(flip);
        boolean flipV = "flipHV".equals(flip) || "flipV".equals(flip);
        String filePath = parts[0];
        if (StringUtil.isEmpty(filePath)) {
          this.resetImage(propertyValue, (Image) null, newAlpha, newFillType, newAnchor);
        } else {
          try {
            URL url = filePath.contains("://") ? new URL(filePath) : (FileUtil.isAbsolutePlatformIndependent(filePath) ? new File(filePath) : new File(PathManager.getConfigPath(), filePath)).toURI().toURL();
            ModalityState modalityState = ModalityState.stateForComponent(rootComponent);
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
              BufferedImageFilter flipFilter = !flipV && !flipH ? null : flipFilter(flipV, flipH);
              Image m = ImageLoader.loadFromUrl(url, true, true, new ImageFilter[]{flipFilter}, ScaleContext.create());
              ApplicationManager.getApplication().invokeLater(() -> {
                this.resetImage(propertyValue, m, newAlpha, newFillType, newAnchor);
              }, modalityState);
            });
          } catch (Exception var12) {
            this.resetImage(propertyValue, (Image) null, newAlpha, newFillType, newAnchor);
          }

        }
      }
    };
  }

  public static AbstractPainter newImagePainter(@NotNull final Image image, @NotNull final Fill fillType, @NotNull final Anchor anchor, final float alpha, @NotNull final Insets insets) {


    return new PaintersHelper.ImagePainter() {
      public boolean needsRepaint() {
        return true;
      }

      public void executePaint(Component component, Graphics2D g) {
        this.executePaint(g, component, image, fillType, anchor, alpha, insets);
      }
    };
  }

  public boolean hasPainters() {
    return !this.myPainters.isEmpty();
  }

  public boolean needsRepaint() {
    Iterator var1 = this.myPainters.iterator();

    Painter painter;
    do {
      if (!var1.hasNext()) {
        return false;
      }

      painter = (Painter) var1.next();
    } while (!painter.needsRepaint());

    return true;
  }

  public void addPainter(@NotNull Painter painter, @Nullable Component component) {

    this.myPainters.add(painter);
    this.myPainter2Component.put(painter, component == null ? this.myRootComponent : component);
    painter.addListener(this);
  }

  public void removePainter(@NotNull Painter painter) {

    painter.removeListener(this);
    this.myPainters.remove(painter);
    this.myPainter2Component.remove(painter);
  }

  public void clear() {
    Iterator var1 = this.myPainters.iterator();

    while (var1.hasNext()) {
      Painter painter = (Painter) var1.next();
      painter.removeListener(this);
    }

    this.myPainters.clear();
    this.myPainter2Component.clear();
  }

  public void paint(Graphics g) {
    this.runAllPainters(g, this.computeOffsets(g, this.myRootComponent));
  }

  void runAllPainters(Graphics gg, @Nullable PaintersHelper.Offsets offsets) {
    if (!this.myPainters.isEmpty() && offsets != null) {
      Graphics2D g = (Graphics2D) gg;
      AffineTransform orig = g.getTransform();
      int i = 0;
      Iterator var6 = this.myPainters.iterator();

      while (var6.hasNext()) {
        Painter painter = (Painter) var6.next();
        if (painter.needsRepaint()) {
          Component cur = (Component) this.myPainter2Component.get(painter);
          g.setTransform(offsets.transform);
          g.translate(offsets.offsets[i++], offsets.offsets[i++]);
          painter.paint(cur, g);
        }
      }

      g.setTransform(orig);
    }
  }

  @Nullable
  PaintersHelper.Offsets computeOffsets(Graphics gg, @NotNull JComponent component) {

    if (this.myPainters.isEmpty()) {
      return null;
    } else {
      PaintersHelper.Offsets offsets = new PaintersHelper.Offsets();
      int i = 0;
      offsets.offsets = new int[this.myPainters.size() * 2];
      Graphics2D g = (Graphics2D) gg;
      offsets.transform = new AffineTransform(g.getTransform());
      Rectangle r = null;
      Component prev = null;
      Iterator var8 = this.myPainters.iterator();

      while (true) {
        while (true) {
          Painter painter;
          do {
            if (!var8.hasNext()) {
              return offsets;
            }

            painter = (Painter) var8.next();
          } while (!painter.needsRepaint());

          Component cur = (Component) this.myPainter2Component.get(painter);
          if (cur == prev && r != null) {
            break;
          }

          Container curParent = cur.getParent();
          if (curParent != null) {
            r = SwingUtilities.convertRectangle(curParent, cur.getBounds(), component);
            prev = cur;
            break;
          }
        }

        offsets.offsets[i++] = r.x;
        offsets.offsets[i++] = r.y;
      }
    }
  }

  public void onNeedsRepaint(Painter painter, JComponent dirtyComponent) {
    if (dirtyComponent != null && dirtyComponent.isShowing()) {
      Rectangle rec = SwingUtilities.convertRectangle(dirtyComponent, dirtyComponent.getBounds(), this.myRootComponent);
      this.myRootComponent.repaint(rec);
    } else {
      this.myRootComponent.repaint();
    }

  }

  private abstract static class ImagePainter extends AbstractPainter {
    final Map<GraphicsConfiguration, PaintersHelper.Cached> cachedMap;

    private ImagePainter() {
      this.cachedMap = ContainerUtil.newHashMap();
    }

    @Nullable
    private static VolatileImage validateImage(@Nullable GraphicsConfiguration cfg, @Nullable VolatileImage image) {
      if (image == null) {
        return null;
      } else {
        boolean lost1 = image.contentsLost();
        int validated = image.validate(cfg);
        boolean lost2 = image.contentsLost();
        if (!lost1 && !lost2 && validated == 0) {
          return image;
        } else {
          PaintersHelper.LOG.info(logPrefix(cfg, image) + "image flushed: contentsLost=" + lost1 + "||" + lost2 + "; validate=" + validated);
          image.flush();
          return null;
        }
      }
    }

    @NotNull
    private static VolatileImage createImage(@Nullable GraphicsConfiguration cfg, int w, int h) {
      GraphicsConfiguration safe = cfg != null ? cfg : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

      VolatileImage image;
      try {
        image = safe.createCompatibleVolatileImage(w, h, new ImageCapabilities(true), 3);
      } catch (Exception var6) {
        image = safe.createCompatibleVolatileImage(w, h, 3);
      }

      image.validate(cfg);
      image.setAccelerationPriority(1.0F);
      ImageCapabilities caps = image.getCapabilities();
      PaintersHelper.LOG.info(logPrefix(cfg, image) + (caps.isAccelerated() ? "" : "non-") + "accelerated " + (caps.isTrueVolatile() ? "" : "non-") + "volatile image created");

      return image;
    }

    @NotNull
    private static String logPrefix(@Nullable GraphicsConfiguration cfg, @NotNull VolatileImage image) {

      String var10000 = "(" + (cfg == null ? "null" : cfg.getClass().getSimpleName()) + ") " + image.getWidth() + "x" + image.getHeight() + " ";

      return var10000;
    }

    @NotNull
    static BufferedImageFilter flipFilter(final boolean flipV, final boolean flipH) {
      BufferedImageFilter var10000 = new BufferedImageFilter(new BufferedImageOp() {
        public BufferedImage filter(BufferedImage src, BufferedImage dest) {
          AffineTransform tx = AffineTransform.getScaleInstance(flipH ? -1.0D : 1.0D, flipV ? -1.0D : 1.0D);
          tx.translate(flipH ? (double) (-src.getWidth((ImageObserver) null)) : 0.0D, flipV ? (double) (-src.getHeight((ImageObserver) null)) : 0.0D);
          AffineTransformOp op = new AffineTransformOp(tx, 1);
          return op.filter(src, dest);
        }

        public Rectangle2D getBounds2D(BufferedImage src) {
          return null;
        }

        public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
          return null;
        }

        public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
          return null;
        }

        public RenderingHints getRenderingHints() {
          return null;
        }
      });

      return var10000;
    }

    public void executePaint(@NotNull Graphics2D g, @NotNull Component component, @NotNull Image image, @NotNull Fill fillType, @NotNull Anchor anchor, float alpha, @NotNull Insets insets) {


      int cw0 = component.getWidth();
      int ch0 = component.getHeight();
      Insets i = JBUI.insets(insets.top * ch0 / 100, insets.left * cw0 / 100, insets.bottom * ch0 / 100, insets.right * cw0 / 100);
      int cw = cw0 - i.left - i.right;
      int ch = ch0 - i.top - i.bottom;
      int w = image.getWidth((ImageObserver) null);
      int h = image.getHeight((ImageObserver) null);
      if (w > 0 && h > 0) {
        GraphicsConfiguration cfg = g.getDeviceConfiguration();
        PaintersHelper.Cached cached = (PaintersHelper.Cached) this.cachedMap.get(cfg);
        VolatileImage scaled = cached == null ? null : cached.image;
        Rectangle src0 = new Rectangle();
        Rectangle dst0 = new Rectangle();
        this.calcSrcDst(src0, dst0, w, h, cw, ch, fillType);
        this.alignRect(src0, w, h, anchor);
        if (fillType == Fill.TILE) {
          this.alignRect(dst0, cw, ch, anchor);
        }

        int sw0 = scaled == null ? -1 : scaled.getWidth((ImageObserver) null);
        int sh0 = scaled == null ? -1 : scaled.getHeight((ImageObserver) null);

        Rectangle r;
        for (boolean repaint = cached == null || !cached.src.equals(src0) || !cached.dst.equals(dst0); (scaled = validateImage(cfg, scaled)) == null || repaint; repaint = false) {
          int sw = Math.min(cw, dst0.width);
          int sh = Math.min(ch, dst0.height);
          if (scaled != null && sw0 >= sw && sh0 >= sh) {
            cached.src.setBounds(src0);
            cached.dst.setBounds(dst0);
          } else {
            scaled = createImage(cfg, sw, sh);
            this.cachedMap.put(cfg, cached = new PaintersHelper.Cached(scaled, src0, dst0));
          }

          Graphics2D gg = scaled.createGraphics();
          gg.setComposite(AlphaComposite.Src);
          if (fillType == Fill.SCALE) {
            gg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            UIUtil.drawImage(gg, image, dst0, src0, (ImageObserver) null);
          } else if (fillType != Fill.TILE) {
            UIUtil.drawImage(gg, image, dst0, src0, (ImageObserver) null);
          } else {
            r = new Rectangle(0, 0, 0, 0);

            for (int x = 0; x < dst0.width; x += w) {
              for (int y = 0; y < dst0.height; y += h) {
                r.setBounds(dst0.x + x, dst0.y + y, src0.width, src0.height);
                UIUtil.drawImage(gg, image, r, src0, (ImageObserver) null);
              }
            }
          }

          gg.dispose();
        }

        long currentTime = System.currentTimeMillis();
        cached.touched = currentTime;
        if (this.cachedMap.size() > 2) {
          this.clearImages(currentTime);
        }

        Rectangle src = new Rectangle(0, 0, cw, ch);
        r = new Rectangle(i.left, i.top, cw, ch);
        if (fillType != Fill.TILE) {
          this.alignRect(src, dst0.width, dst0.height, anchor);
        }

        float adjustedAlpha = Boolean.TRUE.equals(g.getRenderingHint(ADJUST_ALPHA)) ? 0.65F * alpha : alpha;
        GraphicsConfig gc = (new GraphicsConfig(g)).setAlpha(adjustedAlpha);
        UIUtil.drawImage(g, scaled, r, src, (BufferedImageOp) null, (ImageObserver) null);
        gc.restore();
      }
    }

    void calcSrcDst(Rectangle src, Rectangle dst, int w, int h, int cw, int ch, Fill fillType) {
      int dh;
      if (fillType == Fill.SCALE) {
        boolean useWidth = cw * h > ch * w;
        dh = useWidth ? w : cw * h / ch;
        int sh = useWidth ? ch * w / cw : h;
        src.setBounds(0, 0, dh, sh);
        dst.setBounds(0, 0, cw, ch);
      } else if (fillType == Fill.TILE) {
        int dw = cw < w ? w : ((cw / w + 1) / 2 * 2 + 1) * w;
        dh = ch < h ? h : ((ch / h + 1) / 2 * 2 + 1) * h;
        src.setBounds(0, 0, w, h);
        dst.setBounds(0, 0, dw, dh);
      } else {
        src.setBounds(0, 0, Math.min(w, cw), Math.min(h, ch));
        dst.setBounds(src);
      }

    }

    void alignRect(Rectangle r, int w, int h, Anchor anchor) {
      if (anchor != Anchor.TOP_CENTER && anchor != Anchor.CENTER && anchor != Anchor.BOTTOM_CENTER) {
        r.x = anchor != Anchor.TOP_LEFT && anchor != Anchor.MIDDLE_LEFT && anchor != Anchor.BOTTOM_LEFT ? w - r.width : 0;
        r.y = anchor != Anchor.TOP_LEFT && anchor != Anchor.TOP_RIGHT ? (anchor != Anchor.BOTTOM_LEFT && anchor != Anchor.BOTTOM_RIGHT ? (h - r.height) / 2 : h - r.height) : 0;
      } else {
        r.x = (w - r.width) / 2;
        r.y = anchor == Anchor.TOP_CENTER ? 0 : (anchor == Anchor.BOTTOM_CENTER ? h - r.height : (h - r.height) / 2);
      }

    }

    void clearImages(long currentTime) {
      boolean all = currentTime <= 0L;
      Iterator it = this.cachedMap.keySet().iterator();

      while (true) {
        GraphicsConfiguration cfg;
        PaintersHelper.Cached c;
        do {
          if (!it.hasNext()) {
            return;
          }

          cfg = (GraphicsConfiguration) it.next();
          c = (PaintersHelper.Cached) this.cachedMap.get(cfg);
        } while (!all && currentTime - c.touched <= 120000L);

        it.remove();
        PaintersHelper.LOG.info(logPrefix(cfg, c.image) + "image flushed" + (all ? "" : "; untouched for " + StringUtil.formatDuration(currentTime - c.touched)));
        c.image.flush();
      }
    }
  }

  static class Cached {
    final VolatileImage image;
    final Rectangle src;
    final Rectangle dst;
    long touched;

    Cached(VolatileImage image, Rectangle src, Rectangle dst) {
      this.image = image;
      this.src = src;
      this.dst = dst;
    }
  }

  public static class Offsets {
    AffineTransform transform;
    int[] offsets;

    public Offsets() {
    }
  }
}

