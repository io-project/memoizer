package pl.edu.uj.tcs.memoizer.gui;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JToggleButton;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Box;

import java.awt.Component;
import java.awt.Desktop;


import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class JImagePanel extends JPanel {

	/**
	 * Create the panel.
	 * 
	 * TODO sprawdzać czy parametry nie są null'ami
	 */
	public JImagePanel(final Content content) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		//Pole z tytułem
		JLabel title = new JLabel(content.getTitle()==null?"":content.getTitle());
		horizontalBox.add(title);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JToggleButton toggleButton = new JToggleButton("★");//star: ★
		
		//toggleButton.setBorder(null);
		//toggleButton.setIcon(new ImageIcon(JImagePanel.class.getResource("/resources/non-starred.png")));
		//toggleButton.setSelectedIcon(new ImageIcon(JImagePanel.class.getResource("/resources/starred.png")));
		
		toggleButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toggleButton.setIconTextGap(0);
		horizontalBox.add(toggleButton);
		toggleButton.setFont(new Font("Dialog", Font.BOLD, 20));
		
		if(content.isStarred())
			toggleButton.setSelected(!true);
		
		if(content.getDescription()!=null){
			JTextArea description = new JTextArea();
			description.setWrapStyleWord(true);
			description.setLineWrap(true);
			description.setOpaque(false);
			description.setEditable(false);
			description.setText(content.getDescription());
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
		if(content.getImageUrl()!=null){
			JLabel image = new JLabel("");
			image.setIconTextGap(0);
			image.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			ImageIcon imageIcon;
			
			try{
				URL url = new URL(content.getImageUrl());
				BufferedImage icon = ImageIO.read(url);
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
		
		if(content.getUrl()!=null){
			JLabel browserLink = new JLabel("<HTML><A HREF=\"\">open in browser</A></HTML>");
			
			//Listener otwierający stronę z obrazkiem w domyślej przeglądarce
			browserLink.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent ev) {
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				        try {
				            desktop.browse(new URL(content.getUrl()).toURI());
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
	
	public JImagePanel(){
		this(new Content(
    		"Jeden plakat, MILION wspomnień!", 
    		"Kot Tip Top, Rożnowa Pantera, Smerfetka, Kosmiczny Duch, Batman, Janet, Scooby Doo, Muttley, Kudłaty, Bałwan, Dinocco, Goryl Magilla, Pan Jinks, Flinston, Willma Flinston, Hong Kong Phooey, Elroy, Joe Jetson, George Jetson, Becky, TOM, Papa Smerf, Astro, Barney, Pebbles, Bart, Gargamel, Bambam, JERRY!, Wredniak, Johnny Quest, Pixie, Miś Yogi, Wally Gator, Boo Boo, Dixie, Judy i Pies Huckleberry", 
    		"http://retro.pewex.pl/uimages/services/pewex/i18n/pl_PL/201310/1382521263_by_krzys_500.jpg?1382521276", 
    		"http://retro.pewex.pl/477976/Jeden-plakat-MILION-wspomnien"
    	));
	}
}
