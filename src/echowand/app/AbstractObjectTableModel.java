package echowand.app;

import echowand.common.EPC;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Yoshiki Makino
 */
public abstract class AbstractObjectTableModel extends AbstractTableModel {
    public abstract void fireEPCDataUpdated(EPC epc);
    public abstract void refreshCache();
}