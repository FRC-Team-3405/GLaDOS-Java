# Team 3405 Crecendo code  
**GLaDOS Java**  
![image](https://github.com/FRC-Team-3405/GLaDOS-Java/assets/67015041/a61d50ed-417b-4719-aa82-30872542794b)  
our java code for the 2024 season! 
this year we are using a [Swerve Drive Specialties MK4i](https://www.swervedrivespecialties.com/products/mk4i-swerve-module) drive base, with 8 [Falcon 500](https://store.ctr-electronics.com/falcon-500-powered-by-talon-fx/) motors. 
our base code is derived from [this example swerve code](https://github.com/dirtbikerxz/BaseTalonFXSwerve)

## Play Structure
Our robot is designed to be a fast cycle, floor pickup, and speaker shooting bot. We are (planning) on using a 4 bar extension with 2 pairs of bars with 2 inch compliant wheels as our intake. The intake will directly feed to our launcher, which will have 2 pairs of wheels connected with a belt. The launcher itself will be variable angle, allowing us to be able to shoot from up close, and afar. We will have a camera on the front of the launcher to see the april tags on the speaker, and will use auto alignment to shoot.

| Color | Code | Name | Function |
| --- | --- | --- | --- |
| 🟪 Purple | D | Default | Default Mode. Intake in, no note, normal operation. |
| Intake Modes |
| 🟥 Red Blink | IO | Intake Out | Intake out and running, waiting for note pickup, reduced movemnt and rotation speed.|
| 🟥 Red | IL | Intake Loaded | Intake out with note in, ready to retract, slightly reduced movement speed.|
| 🟧 Orange | R | Ready | Intake in with note, searching for speaker april tags |
| Scoring Modes |
| 🟧 Orange Blink | R | Ready | Intake in with note, speaker april tags found, ready to lock |
| 🟨 Yellow | KP | Lock Pending| Locking on speaker, speed greatly reduced, no rotation control |
| 🟩 Green | K | Locked | locked, preparing to shoot. |
| 🟦 Blue | L | Launching | Cycling launcher |
| Endgame Modes | 
| 🟨🟥 Yellow Red | CE | Climber Extending | Extending climber decreased speed |
| 🟨🟧 Yellow Orange | CR | Climber Ready | Climber ready to climb, greatly decreased speed |
| 🟨🟦 Yellow Blue | C | Climbing | Climbing chain, movement disabled |
| 🟨🟩 Yellow Green | C | Climber Locked | Climbing commplete, locked |
