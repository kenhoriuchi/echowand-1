package echowand.object;

import echowand.common.EPC;
import java.util.Random;

/**
 * プロパティ値をランダムに変化させるDelegate
 * @author Yoshiki Makino
 */
public class LocalObjectRandomDelegate implements LocalObjectDelegate {

    private EPC epc;
    private int len;
    private static Random random = new Random();

    /**
     * LocalObjectRandomDelegateを生成する。
     * @param epc ランダムに変化させるプロパティデータのEPC
     * @param len プロパティデータのバイト数
     */
    public LocalObjectRandomDelegate(EPC epc, int len) {
        this.epc = epc;
        this.len = len;
    }

    /**
     * 指定されたEPCが設定されたEPCと等しい時にはランダムのバイト配列を返す。
     * 特に代理処理を行わない場合にはnullを返す。
     * @param object プロパティデータが要求されているオブジェクト
     * @param epc 要求プロパティデータのEPC
     * @return 設定されたEPCの場合にはランダムのバイト配列、その他のEPCの場合はnull
     */
    @Override
    public ObjectData getData(LocalObject object, EPC epc) {
        if (this.epc == epc) {
            byte[] bytes = new byte[len];
            random.nextBytes(bytes);
            return new ObjectData(bytes);
        } else {
            return null;
        }
    }
    
    /**
     * 何も処理せずにfalseを返す。
     * @param object プロパティデータの変更を要求されているオブジェクト
     * @param epc EPCの指定
     * @param data セットするデータの指定
     * @return 常にfalse
     */
    @Override
    public boolean setData(LocalObject object, EPC epc, ObjectData data) {
        return false;
    }

    /**
     * 何も処理を行わない。
     * @param object プロパティデータの変更通知を行っているオブジェクト
     * @param epc プロパティデータに変更のあったEPC
     * @param data 新しいプロパティデータ
     */
    @Override
    public void notifyDataChanged(LocalObject object, EPC epc, ObjectData data) {
    }
}