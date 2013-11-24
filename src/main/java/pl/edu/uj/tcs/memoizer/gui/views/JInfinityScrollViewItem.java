package pl.edu.uj.tcs.memoizer.gui.views;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.Box;

import java.awt.Component;
import java.awt.Desktop;

import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import pl.edu.uj.tcs.memoizer.plugins.Meme;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class JInfinityScrollViewItem extends JPanel {

	/**
	 * Create the panel.
	 * 
	 * TODO sprawdzać czy parametry nie są null'ami
	 */
	public JInfinityScrollViewItem(final Meme meme) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		//Pole z tytułem
		//JLabel title = new JLabel(meme.getTitle()==null?"":meme.getTitle());
		JTextPane title = new JTextPane();
		title.setText(meme.getTitle()==null?"":meme.getTitle());
		title.setBorder(null);
		title.setAutoscrolls(false);
		title.setOpaque(false);
		title.setFont(UIManager.getFont("Label.font"));
		//TODO dodać pionowe centrowanie tytułu !!!
		
		horizontalBox.add(title);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		//Przycisk lubienia
		JToggleButton toggleButton = new JToggleButton("★");//star: ★
		
		//toggleButton.setBorder(null);
		//toggleButton.setIcon(new ImageIcon(JImagePanel.class.getResource("/resources/non-starred.png")));
		//toggleButton.setSelectedIcon(new ImageIcon(JImagePanel.class.getResource("/resources/starred.png")));
		
		toggleButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toggleButton.setIconTextGap(0);
		horizontalBox.add(toggleButton);
		toggleButton.setFont(new Font("Dialog", Font.BOLD, 20));
		
		//TODO dodać obsługę gwiazdkowania
		//if(content.isStarred())
		//	toggleButton.setSelected(!true);
		
		/* pole z opisem */
		if(meme.getDescription()!=null){
			JTextArea description = new JTextArea();
			description.setWrapStyleWord(true);
			description.setLineWrap(true);
			description.setOpaque(false);
			description.setEditable(false);
			description.setText(meme.getDescription());
			add(description);
		}
		
		/**
		 * TODO Zamienić na border
		 */
		Component verticalStrut_1 = Box.createVerticalStrut(6);
		add(verticalStrut_1);
		
		/**
		 * TODO W przypadku problemu z pobraniem obrazka, dodać jakiś placeholder 
		 * TODO Po kliknięciu w obrazek zrobić reload
		 * TODO Dodać resizowanie w przypadku gdy obrazek jest za duży, /wtedy po kliknięciu w niego powinien się powiększyć/
		 */
		if(meme.getImageLink()!=null){
			JLabel image = new JLabel("");
			image.setIconTextGap(0);
			image.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			ImageIcon imageIcon;
			
			try{
				//URL url = new URL(content.getImageUrl());
				URLConnection c = meme.getImageLink().openConnection();
				c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				
				
				BufferedImage icon = ImageIO.read(c.getInputStream());
				imageIcon = new ImageIcon(icon);
				
			}catch(Exception e){
				imageIcon = new ImageIcon(this.getClass().getResource("/icons/garbage.png"));
				e.printStackTrace();
			}
			
			image.setIcon(imageIcon);
			
			image.setHorizontalAlignment(SwingConstants.RIGHT);
			image.setBorder(new EmptyBorder(8,0,8,0));
			add(image);
		}

		//Wspomagający panel dla linków, likeów, itp.
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		panel.setOpaque(false);
		add(panel);
		
		if(meme.getPageLink()!=null){
			JLabel browserLink = new JLabel("<HTML><A HREF=\"\">open in browser</A></HTML>");
			
			//Listener otwierający stronę z obrazkiem w domyślej przeglądarce
			browserLink.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				        try {
				            desktop.browse(meme.getPageLink().toURI());
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				    }
				}
			});
			panel.add(browserLink);
			browserLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			browserLink.setHorizontalAlignment(SwingConstants.LEFT);
		}
	}
}
