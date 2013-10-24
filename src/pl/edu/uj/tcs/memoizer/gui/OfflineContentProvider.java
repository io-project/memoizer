package pl.edu.uj.tcs.memoizer.gui;

import java.util.Random;

public class OfflineContentProvider implements ContentProvider {
	private Random rand = new Random();
	private Content[] someContents = {
		new Content(
    		"Jeden plakat, MILION wspomnień!", 
    		"Kot Tip Top, Rożnowa Pantera, Smerfetka, Kosmiczny Duch, Batman, Janet, Scooby Doo, Muttley, Kudłaty, Bałwan, Dinocco, Goryl Magilla, Pan Jinks, Flinston, Willma Flinston, Hong Kong Phooey, Elroy, Joe Jetson, George Jetson, Becky, TOM, Papa Smerf, Astro, Barney, Pebbles, Bart, Gargamel, Bambam, JERRY!, Wredniak, Johnny Quest, Pixie, Miś Yogi, Wally Gator, Boo Boo, Dixie, Judy i Pies Huckleberry", 
    		"http://retro.pewex.pl/uimages/services/pewex/i18n/pl_PL/201310/1382521263_by_krzys_500.jpg?1382521276", 
    		"http://retro.pewex.pl/477976/Jeden-plakat-MILION-wspomnien"
    	),
    	new Content(
    		"Ale przecież w Polsce", 
    		"Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies. Curabitur et ligula. Ut molestie a, ultricies porta urna. Vestibulum commodo volutpat a, convallis ac, laoreet enim. Phasellus fermentum in, dolor.", 
    		"http://kotburger.pl/uimages/services/kotburger/i18n/pl_PL/201310/1382454282_by_Bonia_500.jpg?1382454282", 
    		"http://demotywatory.pl/4224640/Ale-przeciez-w-Polsce"
    	),
    	new Content(
    		"Co robi Jezus na rondzie",
    		null,
    		"http://fabrykamemow.pl/uimages/services/fabrykamemow/i18n/pl_PL/201310/1382207285_by_ADR373_500.jpg?1382207285",
    		"http://fabrykamemow.pl/memy/235621/Co-robi-Jezus-na-rondzie"
    	),
    	new Content(
    		"NoName",
    		null,
    		null,
    		null
    	)
	};
	
	@Override
	public Content getNext() {
		return someContents[rand.nextInt(4)];
	}

}
