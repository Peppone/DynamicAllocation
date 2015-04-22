package input;


public class EmptyState extends State {
	public EmptyState(int vm, int serv, int spr, int rpp) {
		super(vm, serv, spr, rpp);
		for (int i = 0; i < server; ++i) {
			for (int j = 0; j < objectives; ++j) {
				servResource[i][j] = 0;
			}
			servBW[i] = 0;
		}
		for (int i = 0; i < torSwitch; ++i) {
			rackBW[i] = 0;
		}

	}
}