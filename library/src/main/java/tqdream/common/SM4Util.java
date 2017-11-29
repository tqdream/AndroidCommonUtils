/**
 * �Գ��㷨���ʹ��demo
 * ��Ҫ�ӽ���
 */
package tqdream.common;

import com.kjhxtc.crypto.api.Symmetric.BocSymmetricCrypt;
import com.kjhxtc.crypto.api.Symmetric.SymDecryptOperator;
import com.kjhxtc.crypto.api.Symmetric.SymEncryptOperator;
import com.kjhxtc.crypto.util.encoder.Hex;

import javax.crypto.IllegalBlockSizeException;

/**
 * @author taoqiang @create on 2016/10/31 14:09
 * @email tqdream@163.com
 */
public class SM4Util {

    /**
     * SMS4加密
     */
    public static byte[] encodeSMS4(byte[] plaintext, byte[] key) {
        BocSymmetricCrypt api = new BocSymmetricCrypt();
        SymEncryptOperator sm4opt = api.BOC_EncryptInit(BocSymmetricCrypt.ALG_SM4_ECB, BocSymmetricCrypt.PADDING_PKCS7, key, null);
        byte[] e_data = api.BOC_Encrypt(sm4opt, plaintext);

        return e_data;
    }

    /**
     * SMS4解密
     */
    public static byte[] decodeSMS4(byte[] ciphertext, byte[] key) {
        BocSymmetricCrypt api = new BocSymmetricCrypt();
        SymDecryptOperator sm4dec = api.BOC_DecryptInit(BocSymmetricCrypt.ALG_SM4_ECB, BocSymmetricCrypt.PADDING_PKCS7, key, null);
        byte[] d_data = null;
        try {
            d_data = api.BOC_Decrypt(sm4dec, ciphertext);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return d_data;
    }
}
