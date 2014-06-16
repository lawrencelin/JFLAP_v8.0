package util.view.thinscroller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;


import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ThinScrollBarUI extends BasicScrollBarUI {

	public ThinScrollBarUI() {
		super();
	}

	@Override
	protected void installComponents() {
		super.installComponents();
		 decrButton.setVisible(false);
		 decrButton.setPreferredSize(new Dimension(0,0));
		 incrButton.setVisible(false);
		 incrButton.setPreferredSize(new Dimension(0,0));
	}

	@Override
	protected void layoutHScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();

        /* Height and top edge of the buttons and thumb.
         */
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();

        int leftLimitX = sbInsets.left;
        int rightLimitX = sbSize.width - sbInsets.right;

        /* The thumb must fit within the width left over after we
         * subtract the preferredSize of the buttons and the insets.
         */
        int sbInsetsW = sbInsets.left + sbInsets.right;
//        int sbButtonsW = leftButtonW + rightButtonW;
        float trackW = sbSize.width;

        /* Compute the width and origin of the thumb.  Enforce
         * the thumbs min/max dimensions.  The case where the thumb 
         * is at the right edge is handled specially to avoid numerical 
         * problems in computing thumbX.  If the thumb doesn't
         * fit in the track (trackH) we'll hide it later.
         */
        float min = sb.getMinimum();
        float max = sb.getMaximum();
        float extent = sb.getVisibleAmount();
        float range = max - min;
        float value = sb.getValue();

        int thumbW = (range <= 0) ? getMaximumThumbSize().width
                : (int) (trackW * (extent / range));
        thumbW = Math.max(thumbW, getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, getMaximumThumbSize().width);

        int thumbX = ltr ? rightLimitX - thumbW : leftLimitX;
        if (value < (max - sb.getVisibleAmount())) {
            float thumbRange = trackW - thumbW;
            if (ltr) {
                thumbX = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
            } else {
                thumbX = (int) (0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
            }
            thumbX += leftLimitX;
        }

        /* Update the trackRect field.
         */
        int itrackX = leftLimitX;
        int itrackW = rightLimitX - itrackX;
        trackRect.setBounds(itrackX, itemY, itrackW, itemH);

        /* Make sure the thumb fits between the buttons.  Note 
         * that setting the thumbs bounds causes a repaint.
         */
        if (thumbW >= (int) trackW) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                // This is used primarily for GTK L&F, which expands the
                // thumb to fit the track when it would otherwise be hidden.
                setThumbBounds(itrackX, itemY, itrackW, itemH);
            } else {
                // Other L&F's simply hide the thumb in this case.
                setThumbBounds(0, 0, 0, 0);
            }
        } else {
            if (thumbX + thumbW > rightLimitX) {
                thumbX = rightLimitX - thumbW;
            }
            if (thumbX < leftLimitX) {
                thumbX = leftLimitX + 1;
            }
            setThumbBounds(thumbX, itemY, thumbW, itemH);
        }
    }
	
	public Dimension getPreferredSize(JComponent c) {
        return new Dimension(c.getWidth(), c.getHeight());
    }


}
