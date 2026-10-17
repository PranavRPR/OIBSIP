import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UITheme {
    public static final Color BG_TOP      = new Color(0x0F172A);
    public static final Color BG_BOTTOM   = new Color(0x1E293B);
    public static final Color CARD        = Color.WHITE;
    public static final Color PRIMARY     = new Color(0x6366F1);
    public static final Color PRIMARY_HOV = new Color(0x4F46E5);
    public static final Color DANGER      = new Color(0xEF4444);
    public static final Color DANGER_HOV  = new Color(0xDC2626);
    public static final Color MUTED_BTN   = new Color(0x64748B);
    public static final Color MUTED_BTN_H = new Color(0x475569);
    public static final Color TEXT        = new Color(0x0F172A);
    public static final Color TEXT_SOFT   = new Color(0x64748B);
    public static final Color BORDER      = new Color(0xE2E8F0);
    public static final Color FIELD_BG    = new Color(0xF8FAFC);

    public static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font F_HEAD   = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font F_BTN    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font F_MONO   = new Font("Consolas", Font.PLAIN, 13);

    private UITheme() {}

    public static class GradientPanel extends JPanel {
        public GradientPanel() { setOpaque(true); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, BG_TOP, getWidth(), getHeight(), BG_BOTTOM));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    public static class CardPanel extends JPanel {
        private final int radius;
        public CardPanel() { this(20); }
        public CardPanel(int r) {
            this.radius = r;
            setOpaque(false);
            setBorder(new EmptyBorder(28, 32, 28, 32));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(6, 8, getWidth() - 14, getHeight() - 14, radius, radius);
            g2.setColor(CARD);
            g2.fillRoundRect(0, 0, getWidth() - 14, getHeight() - 14, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class PrimaryButton extends JButton {
        private final Color base, hover;
        private final int radius = 10;
        public PrimaryButton(String text) { this(text, PRIMARY, PRIMARY_HOV, Color.WHITE); }
        public PrimaryButton(String text, Color base, Color hover, Color fg) {
            super(text);
            this.base = base; this.hover = hover;
            setForeground(fg);
            setFont(F_BTN);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(11, 22, 11, 22));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { repaint(); }
                @Override public void mouseExited(MouseEvent e) { repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = getModel().isRollover() ? hover : base;
            if (getModel().isPressed()) c = c.darker();
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static void paintRoundedField(JTextField field, Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int radius = 10;
        g2.setColor(FIELD_BG);
        g2.fillRoundRect(0, 0, field.getWidth() - 1, field.getHeight() - 1, radius, radius);
        g2.setColor(field.isFocusOwner() ? PRIMARY : BORDER);
        g2.setStroke(new BasicStroke(field.isFocusOwner() ? 2f : 1f));
        g2.drawRoundRect(0, 0, field.getWidth() - 1, field.getHeight() - 1, radius, radius);
        g2.dispose();
    }

    public static class TextInput extends JTextField {
        public TextInput(int cols) {
            super(cols);
            setFont(F_INPUT);
            setForeground(TEXT);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 14, 10, 14));
        }
        @Override protected void paintComponent(Graphics g) {
            paintRoundedField(this, g);
            super.paintComponent(g);
        }
    }

    public static class PasswordInput extends JPasswordField {
        public PasswordInput(int cols) {
            super(cols);
            setFont(F_INPUT);
            setForeground(TEXT);
            setOpaque(false);
            setEchoChar('\u2022');
            setBorder(new EmptyBorder(10, 14, 10, 14));
        }
        @Override protected void paintComponent(Graphics g) {
            paintRoundedField(this, g);
            super.paintComponent(g);
        }
    }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_SOFT);
        return l;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_HEAD);
        l.setForeground(TEXT);
        return l;
    }
}