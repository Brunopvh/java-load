package type_utils.save;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SaveTable {

    public void toExcel(OutputStream os) throws IOException;
    public void toJson(OutputStream os);

}
