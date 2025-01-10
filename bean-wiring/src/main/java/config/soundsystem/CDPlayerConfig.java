package config.soundsystem;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import soundsystem.CompactDisc;
import soundsystem.HighSchoolRapper2Final;

@Configuration //빈 생성이 되더라도 이걸 안 붙이면 DI가 안 됨 
@ComponentScan(basePackages= {"soundsystem"})
public class CDPlayerConfig {
}