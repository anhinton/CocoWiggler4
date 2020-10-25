# TODO

  + migrate Input Handling from Polling to Event Handling
  
  + add Back/Menu button to GameScreen (Input.Keys.BACK/ESC on iOS)
  
  + add buttons to Splash Screen
      - Play
      - Settings
      - Quit (Android/Desktop only)
  
  + create SettingsScreen
      - Music Volume
      - Sound Volume
      - Credits
      
  + config IOSLauncher
      - allowIpod = true
      - orientationLandscape = true
      - orientationPortrait = false
      - useAccelerometer = false
      - useCompass = false

  + config ios/Info.plist.xml
      - force portrait mode on iPad
> <key>UISupportedInterfaceOrientations</key>
<array>
    <string>UIInterfaceOrientationLandscapeRight</string>
    <string>UIInterfaceOrientationLandscapeLeft</string>
</array>
<key>UIRequiresFullScreen</key>
<true/>
  
## Done