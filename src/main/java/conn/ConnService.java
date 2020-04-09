package conn;

import core.thread.Department;
import core.thread.Service;

public class ConnService extends Service {

    public ConnService(Department department, String id) {
        super( department, id );
    }

    @Override
    protected void pulseOverride() {

    }
}
