package config.soundsystem;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import soundsystem.CompactDisc;
import soundsystem.HighSchoolRapper2Final;

@Configuration
@ComponentScan(basePackages= {})
public class CDPlayerConfig {
	public CompactDisc compactDisc() {
		return new HighSchoolRapper2Final();
	}
}
