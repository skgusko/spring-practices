package config.videosystem.mixing;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import videosystem.DVDPlayer;
import videosystem.DigitalVideoDisc;

/**
 * JavaConfig2(DVDPlayerConfig) <-- JavaConfig1(DVDConfig)
 */


@Configuration
@Import({DVDConfig.class})
public class DVDPlayerConfig {
	@Bean
	public DVDPlayer dvdPlayer(@Qualifier("avengers")DigitalVideoDisc dvd) {
		return new DVDPlayer(dvd);
	}
}
