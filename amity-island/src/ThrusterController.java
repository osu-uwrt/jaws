import processing.core.*;

public class ThrusterController {

	PApplet processing;
	RobotModel robot;
	GamepadModel gamepad;

	float X, Y, Z;
	float x_prime, y_prime;
	float L_x, R_x;
	float L_theta, R_theta;
	float L, R, Back;

	ThrusterController(PApplet p, RobotModel bot, GamepadModel ctrl) {
		processing = p;
		robot = bot;
		gamepad = ctrl;
	}

	void update() {

		// Read gamepad values.
		X = gamepad.getRightVertical();
		Y = gamepad.getRightHorizontal();
		Z = gamepad.getLeftVertical();

		// Convert gamepad values to ?
		x_prime = (X + Y) * 0.707f; // constant?
		y_prime = (Y - X) * 0.707f; // constant?
		R_x = y_prime;
		L_x = -x_prime;

		// ?
		vector_control();

		// rightmotor(R, R_theta);
		robot.setPortThrusterPower(motorPower(R));
		robot.setPortThrusterAngle(motorAngle(R_theta));

		// leftmotor(L, L_theta);
		robot.setStbdThrusterPower(motorPower(L));
		robot.setStbdThrusterAngle(motorAngle(L_theta));

		// Zmotor(Back);
		robot.setAftThrusterPower((int) PApplet.map(Back, -1, 1, 22, 240));

	}

	String motorControl(int address_, int motor_, int power) {
		byte thing1_;
		int speed_;
		int thing2_;
		if (motor_ == 1) {
			if (power >= 128) {
				thing1_ = (byte) 0;
				speed_ = power - 127;
				thing2_ = (speed_ + address_) & 127;
			} else {
				thing1_ = 1;
				speed_ = 127 - power;
				thing2_ = (speed_ + address_ + 1) & 127;
			}
		} else { // motor_ == 2
			if (power >= 128) {
				thing1_ = 4;
				speed_ = power - 127;
				thing2_ = (speed_ + address_ + 4) & 127;
			} else {
				thing1_ = 5;
				speed_ = 127 - power;
				thing2_ = (speed_ + address_ + 5) & 127;
			}
		}

		byte speed__ = (byte) (speed_);
		byte address__ = (byte) (address_);
		byte thing2__ = (byte) (thing2_);

		String out = " \"\\x" + PApplet.hex(address__) + "\" \"\\x" + PApplet.hex(thing1_)
				+ "\" \"\\x" + PApplet.hex(speed__) + "\" \"\\x" + PApplet.hex(thing2__) + "\"";

		return out;
	}

	void vector_control() {

		float Cg_ratio = (float) .6422;
		float L_z, R_z;

		// Find front z power.
		R_z = Z / (2 + 2 * Cg_ratio);
		L_z = R_z; // possibly put roll control here

		// Find back power.
		Back = Z * (1 - 1 / (1 + Cg_ratio));

		// Put the motor thrust curve equation here.
		Back = Back;

		if (R_x == 0) {
			R_theta = PApplet.PI / 2;
		} else {
			R_theta = PApplet.atan(R_z / R_x);
		}
		if (L_x == 0) {
			L_theta = PApplet.PI / 2;
		} else {
			L_theta = PApplet.atan(L_z / L_x);
		}
		if (L_theta < 0) {
			L_theta = L_theta + PApplet.PI;
		}
		if (R_theta < 0) {
			R_theta = R_theta + PApplet.PI;
		}

		L_theta = PApplet.degrees(L_theta);

		R_theta = PApplet.degrees(R_theta);

		R = PApplet.sqrt(R_x * R_x + R_z * R_z);
		L = PApplet.sqrt(L_x * L_x + L_z * L_z);

		// Put in motor direction.
		if (R_z < 0) {
			R = -R;
		} else if (R_z == 0) {
			if (R_x < 0) {
				R = -R;
			}
			if (L_x < 0) {
				L = -L;
			}
		}
	}

	int motorPower(float oldPower) {
		int newPower = (int) oldPower * 120;
		if (oldPower > 0)
			newPower += 128;
		else
			newPower -= 128;
		return newPower;
	}

	int motorAngle(float oldAngle) {
		return (int) PApplet.map(oldAngle, 0, 180, 550000, 2450000);
	}
//
//	String convertToString() {
//
//		String commandString = robot.getStbdThrusterAngle() + " "
//				+ robot.getPortThrusterAngle() + " " + robot.getCameraPanAngle()
//				+ " " + robot.getCameraTiltAngle() + " "
//				+ motorControl(128, 1, robot.getStbdThrusterPower()) + " "
//				+ motorControl(128, 2, robot.getPortThrusterPower());
//
//		return commandString;
//
//	}
	String convertToString() {

		String commandString = motorControl(128, 2, robot.getPortThrusterPower());

		return commandString;

	}
	String convertToString2() {

		String commandString = motorControl(128, 1, robot.getStbdThrusterPower());

		return commandString;

	}

}