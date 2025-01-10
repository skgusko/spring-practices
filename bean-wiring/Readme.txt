1. soundsystem: Automatic configuration(Annotation scanning)

[src/main/java]
    soundsystem (자동)
       |--- CDPlayer.java
       |--- CompactDisc.java
       |--- HighSchoolRapper2Final.java

    config.soundsystem (자바 설정)
       |--- CDPlayerConfig.java

[src/main/resources]
    config.soundsystem (xml 설정)
       |--- applicationContext.xml
       
[src/test/java]
    config.soundsystem
       |--- CDPlayerXmlConfigTest.java
       |--- CDPlayerJavaConfigTest.java



=======================================================
2. videosystem: Explicit configuration(Bean configuration)

    videosystem
       |--- DVDPlayer.java
       |--- DigitalVideoDisc.java
       |--- Avengers

    config.videosystem
       |--- DVDPlayerConfig.java(핵심. 빈 설정을 자바로 하는 법)

    config.videosystem.mixing
       |--- DVDConfig.java
       |--- DVDPlayerConfig.java
       |--- VideoSystemConfig.java

[src/main/resources]

    config.videosystem
       |--- applicationContext.xml


[src/test/java]

    config.videosystem
       |--- DVDPlayerJavaConfigTest.java
       |--- DVDPlayerXmlConfigTest.java

    config.videosystem.mixing
       |--- DVDPlayerMixingConfigTest01.java
       |--- DVDPlayerMixingConfigTest02.java
