/*******************************************************************
 *                                                                 *
 *   Functions:                                                    *
 *   vector_control() - Determines a thrust vector.                *
 *   Zmotor/leftmotor/rightmotor - Determines bytes to transmit.   *
 *                                                                 *
 *******************************************************************/

void vector_control() {

  float Cg_ratio=.6422;
  float L_z, R_z;

  // Find front z power.
  R_z = Z / ( 2 + 2 * Cg_ratio );
  L_z = R_z; //possibly put roll control here

  // Find back power.
  Back = Z * ( 1 - 1 / ( 1 + Cg_ratio ) );

  // Put the motor thrust curve equation here.
  Back = Back;

  if ( R_x == 0 ) {
    R_theta = PI / 2;
  }
  else {
    R_theta = atan( R_z / R_x );
  }
  if ( L_x == 0 ) {
    L_theta = PI / 2;
  }
  else {
    L_theta = atan( L_z / L_x );
  }
  if ( L_theta < 0 ) {
    L_theta=L_theta+PI;
  }
  if ( R_theta < 0 ) {
    R_theta = R_theta + PI;
  }

  L_theta = degrees( L_theta );
  R_theta = degrees( R_theta );

  R = sqrt( R_x * R_x + R_z * R_z );
  L = sqrt( L_x * L_x + L_z * L_z );

  // Put in motor direction.
  if ( R_z < 0 ) {
    R = -R;
  }
  else if ( R_z == 0 ) {
    if ( R_x < 0 ) {
      R = -R;
    }
    if ( L_x < 0 ) {
      L = -L;
    }
  }
}

byte aft_thruster_motor_power4, aft_thruster_motor_powerB, port_thruster_motor_power, starboard_thruster_motor_power;
byte port_thruster_servo_angle, starboard_thruster_servo_angle, claw_servo;
int cam_servo_x, cam_servo_y;
int check;

// Provides motor power inputs.
void Zmotor( float power ) {
  if (power>0) {
  aft_thruster_motor_power4=byte( int( map( power, 0, 1, 0, 254 ) ) );
  aft_thruster_motor_powerB=byte(0);
  }
  else {
      aft_thruster_motor_powerB=byte( int( map( power, 0, -1, 0,254 ) ) );
      aft_thruster_motor_power4=byte(0);
  }
}
void leftmotor( float power, float angle ) {
  if ( power > 0 ) {
    port_thruster_motor_power = byte( power * 120 + 128 );
  } 
  else {
    port_thruster_motor_power = byte( power * 120 - 128 );
  }
  port_thruster_servo_angle = byte( angle );//int(map(angle,0,180,0,255));
}
void rightmotor( float power, float angle ) {
  if ( power > 0 ) {
    starboard_thruster_motor_power = byte( power * 120 + 128 );
  }
  else {
    starboard_thruster_motor_power = byte( power * 120 - 128 );
  }
  starboard_thruster_servo_angle = byte( angle );//int(map(angle,0 180,0,255));
}
