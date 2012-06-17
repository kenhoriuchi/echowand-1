package echowand.app;

import echowand.common.EOJ;
import echowand.common.EPC;
import echowand.common.PropertyMap;
import echowand.object.*;
import java.util.EnumMap;

/**
 *
 * @author Yoshiki Makino
 */
public class CachedRemoteObject implements EchonetObject {

    private RemoteObject remoteObject;
    private EnumMap<EPC, ObjectData> dataCache;

    public CachedRemoteObject(RemoteObject remoteObject) {
        this.remoteObject = remoteObject;
        dataCache = new EnumMap<EPC, ObjectData>(EPC.class);
    }

    public void clearCache() {
        dataCache.clear();
    }
    
    public void clearCache(EPC epc) {
        dataCache.remove(epc);
    }

    public boolean isCached(EPC epc) {
        return (dataCache.get(epc) != null);
    }

    public boolean isPropertyMapsCached() {
        return isCached(EPC.x9F)
                && isCached(EPC.x9E)
                && isCached(EPC.x9D);
    }

    public void setCachedData(EPC epc, ObjectData data) {
        dataCache.put(epc, data);
    }

    public boolean updateCache(EPC epc) throws EchonetObjectException {
        setCachedData(epc, remoteObject.getData(epc));
        return isCached(epc);
    }
    
    public void observeData(EPC epc) throws EchonetObjectException {
        remoteObject.observeData(epc);
    }

    public boolean updatePropertyMapsCache() throws EchonetObjectException {
        return updateCache(EPC.x9F)
                && updateCache(EPC.x9E)
                && updateCache(EPC.x9D);
    }

    private boolean isEPCSetAtPropertyMap(EPC epc, EPC propertyMapEpc) {
        if (!isCached(propertyMapEpc)) {
            return false;
        }

        return new PropertyMap(getData(propertyMapEpc).toBytes()).isSet(epc);
    }

    @Override
    public boolean isGettable(EPC epc) {
        return isEPCSetAtPropertyMap(epc, EPC.x9F);
    }

    @Override
    public boolean isSettable(EPC epc) {
        return isEPCSetAtPropertyMap(epc, EPC.x9E);
    }

    @Override
    public boolean isObservable(EPC epc) {
        return isEPCSetAtPropertyMap(epc, EPC.x9D);
    }

    public boolean isValidEPC(EPC epc) {
        return isGettable(epc)
                || isSettable(epc)
                || isObservable(epc);
    }

    @Override
    public ObjectData getData(EPC epc) {
        if (!isCached(epc)) {
            return null;
        }

        return dataCache.get(epc);
    }

    public int size() {
        if (!isPropertyMapsCached()) {
            return 0;
        }

        int count = 0;

        for (byte code = (byte) 0x80; code <= (byte) 0xff; code++) {
            EPC epc = EPC.fromByte(code);
            if (isValidEPC(epc)) {
                count++;
            }
        }

        return count;
    }

    public EPC getEPC(int index) {
        if (!isPropertyMapsCached()) {
            return null;
        }

        int count = 0;
        for (byte code = (byte) 0x80; code <= (byte) 0xff; code++) {
            EPC epc = EPC.fromByte(code);
            if (isValidEPC(epc)) {
                if (index == count) {
                    return epc;
                }
                count++;
            }
        }
        return EPC.Invalid;
    }

    public int getIndexOfEPC(EPC epc) {
        int size = size();

        for (int i = 0; i < size; i++) {
            if (epc.equals(getEPC(i))) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public EOJ getEOJ() {
        return remoteObject.getEOJ();
    }

    @Override
    public boolean contains(EPC epc) throws EchonetObjectException {
        return remoteObject.contains(epc);
    }

    @Override
    public boolean setData(EPC epc, ObjectData data) throws EchonetObjectException {
        boolean ret = remoteObject.setData(epc, data);
        clearCache(epc);
        return ret;
    }

    public void addObserver(RemoteObjectObserver observer) {
        remoteObject.addObserver(observer);
    }
    
    public void removeObserver(RemoteObjectObserver observer) {
        remoteObject.removeObserver(observer);
    }
}