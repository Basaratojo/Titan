package titan.impl;

import titan.RateInterface;
import titan.StateInterface;

public class State implements StateInterface {

	Vector3d position = new Vector3d();
	Vector3d velocity = new Vector3d();

	public State(double x, double y, double z, double vx, double vy, double vz) {
		position.setX(x);
		position.setY(y);
		position.setZ(z);
		velocity.setX(vx);
		velocity.setY(vy);
		velocity.setZ(vz);
	}

	@Override
	public StateInterface addMul(double step, RateInterface rate) {

		Rate arate = (Rate) rate;
		position = (Vector3d) position.addMul(step, arate.speedy());
		return this;
	}

}
