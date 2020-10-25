# TODO

  + ~~migrate touch detection from Coco.update() to GameScreen~~
      - ~~create Coco.setTarget(Vector3) method~~
      - ~~Mouse/touch-screen movement to GameScreen.touchDown()~~
          - ~~call Coco.setTarget(Vector3)~~
      - ~~Keyboard movement to GameScreen.keyDown() and GameScreen.keyUp()~~
          - ~~create setters in Coco() for movement direction~~
          - ~~handle opposite key presses in Coco.move()~~
  
  + add Back/Menu button to GameScreen (Input.Keys.BACK/ESC on iOS)
  
  + add buttons to Splash Screen
      - Play
          - move from keyDown() logic
      - Settings
      - Quit (Android/Desktop only)
  
  + create SettingsScreen
      - Music Volume
      - Sound Volume
      - Credits
  
## Done

  + ~~migrate Input Handling from Polling to Event Handling~~
      - ~~SplashScreen implements InputProcessor~~
          - ~~Gdx.input.setInputProcessor() in constructor~~
          - ~~move ESC/BACK to keyDown()~~
          - ~~move touch detection to touchDown()~~
      - ~~GameScreen implements InputProcessor~~
          - ~~Gdx.input.setInputProcessor() in constructor~~
          - ~~move ESC/BACK to keyDown()~~

  + ~~config ios/Info.plist.xml~~
      - ~~force portrait mode on iPad~~
      
  + ~~config IOSLauncher~~
      - ~~allowIpod = true~~
      - ~~orientationLandscape = true~~
      - ~~orientationPortrait = false~~
      - ~~useAccelerometer = false~~
      - ~~useCompass = false~~