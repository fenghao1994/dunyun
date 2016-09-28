package net.dunyun.framework.android.mainapp.gate;

import com.psoft.bluetooth.utils.CrcUtil;
import com.psoft.bluetooth.utils.HexUtil;
import com.psoft.bluetooth.utils.LogUtil;

import net.dunyun.framework.android.mainapp.util.AesUtilGate;
import net.dunyun.framework.android.mainapp.util.CRC16;

/**
 * @author chenzp
 * @date 2016/6/29
 * @Copyright:重庆平软科技有限公司
 */
public class GateCode {
    /**
     * @return
     */
    public static byte[] getOpenGateData(String gateName, String keyIndex, String gatePort, String inOut) {
        String gateData = gateName + "," + keyIndex + "," + gatePort + "," + inOut;
        byte[] gateDataBytes = gateData.getBytes();
        //0c长度                       0560 CRC ae结尾
        //a5 01 80 11 ff ff ff e0 ff ff ff c0 00 10 01 0c 67617465 30312c30 2c302c31 0560ae
        //a5018011 fffffff0 ffffffc0 0010010d 67617465 30312c31 302c302c 3125caae
        byte[] data = new byte[19 + gateDataBytes.length];
        byte[] temp = {(byte) 0xa5, 0x01, (byte) 0x80, 0x11, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xf0, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xc0, 0x00, 0x10, 0x01};
        System.arraycopy(temp, 0, data, 0, temp.length);
        data[15] = (byte) gateDataBytes.length;

        System.arraycopy(gateDataBytes, 0, data, 16, gateDataBytes.length);

        data[19 + gateDataBytes.length - 1] = (byte) 0xae;

        byte[] crcbyte = new byte[19 + gateDataBytes.length - 4];
        System.arraycopy(data, 1, crcbyte, 0, crcbyte.length);

        byte[] crc = CrcUtil.getCrc16(crcbyte, crcbyte.length);
        data[19 + gateDataBytes.length - 3] = crc[0];
        data[19 + gateDataBytes.length - 2] = crc[1];

        byte[] dataTemp = new byte[data.length - 2];
        System.arraycopy(data, 1, dataTemp, 0, dataTemp.length);
        LogUtil.d("dataTemp1-" + HexUtil.byteToString(dataTemp));

        String encryptContentStr = HexUtil.byteToString(dataTemp).replaceAll("AA", "AA 0A");
        String encryptContentStr1 = encryptContentStr.replaceAll("AE", "AA 0E");
        String encryptContentStr2 = encryptContentStr1.replaceAll("A5", "AA 05").replaceAll(" ", "");
        dataTemp = HexUtil.HexString2Bytes(encryptContentStr2);

        LogUtil.d("dataTemp2-" + HexUtil.byteToString(dataTemp));
        byte[] finalData = new byte[dataTemp.length + 2];
        System.arraycopy(dataTemp, 0, finalData, 1, dataTemp.length);

        finalData[0] = (byte) 0xa5;
        finalData[finalData.length - 1] = (byte) 0xaE;

        LogUtil.d(HexUtil.byteToString(finalData));


        LogUtil.d(HexUtil.byteToString(data));
        return finalData;
    }

    public static byte[] getData(String password, String aes128key, String keyIndex, String door) {
//        A5 01 80 11 {ff ff ff e0} {00 00 00 01}  80 [{源密码，8字节}  {00 00 00 01} {10 01}}{02} {f0 01}] 00 00 ae
//                                      源地址                             流水号                          crc
//  a5 ff 80 11 ffffffe0 00000001 80 b111e8 18cef5f4 a61e57b7 311e5a87 b92fbe01 f53f272b 89bc6bc3 a90f846e 37fa2eae
//A5 FF 80 11 FF FF FF E0 00 00 00 01 80 B1 11 E8 18 CE F5 F4 A6 1E 57 B7 31 1E 5A 87 B9 FF A0 5D 14 73 72 18 B9 9E A0 8F B3 F4 DA 1C 9F EA B3 AE
//A5 FF 80 11 FF FF FF E0 00 00 00 01 80 B1 11 E8 18 CE F5 F4 A6 1E 57 B7 31 1E 5A 87 B9 FF A0 5D 14 73 72 18 B9 9E A0 8F B3 F4 DA 1C 9F EA B3 AE
//B1 11 E8 18 CE F5 F4 A6 1E 57 B7 31 1E 5A 87 B9 2F BE 01 F5 3F 27 2B 89 BC 6B C3 A9 0F 84 6E 37
//A5 FF 80 11 FF FF FF E0 00 00 00 01 80 B1 11 E8 18 CE F5 F4 A6 1E 57 B7 31 1E 5A 87 B9 2F BE 01 F5 3F 27 2B 89 BC 6B C3 A9 0F 84 6E 37 FA 2E AE

//A5 FF 80 11 FF FF FF E0 00 00 00 01 80 AF 5D A8 1D CE 40 34 C6 27 05 07 16 C4 96 82 A7 0E 42 B1 6F A7 F1 38 DF 65 B3 E3 0A 19 EE 6A 4A 3B 98 AE
// A5 FF 40 11 00 00 00 01 FF FF FF E0 80 [E1 89 2F 9C A9 58 DB 47 AA 05 63 6D EE 0C 33 34 42] 57 DD AE
        //1001

        byte[] content = new byte[17];
        byte[] passwordByte = password.getBytes();
        byte[] aes128keyByte = aes128key.getBytes();

        for (int i = 0; i < passwordByte.length; i++) {
            content[i] = passwordByte[i];
        }
        content[8] = 0x00;
        content[9] = 0x00;
        content[10] = 0x00;
        content[11] = 0x01;

        content[12] = 0x10;
        content[13] = 0x01;
        content[14] = 0x02;

        content[15] = (byte) 0x00;
        try{
            content[15] = (byte) Integer.parseInt(door);
        }catch (Exception e){
            e.printStackTrace();
        }
        content[16] = (byte) 0x01;
//        LogUtil.d("--加密前数据---"+HexUtil.byteToString(content));
        byte[] encryptContent = AesUtilGate.encrypt(content, aes128keyByte);
//        LogUtil.d("--加密后数据---"+HexUtil.byteToString(encryptContent));
        byte[] data = new byte[16 + encryptContent.length];
        byte[] startByte = {(byte) 0xa5, (byte) 0xff, (byte) 0x80, 0x11, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xe0, 0x00, 0x00, 0x00, 0x04, (byte) 0x80};

        try {
            int keyIndexInt = Integer.parseInt(keyIndex);
            byte[] keyIndexBytes = HexUtil.intToBytes(keyIndexInt);
            startByte[8] = keyIndexBytes[0];
            startByte[9] = keyIndexBytes[1];
            startByte[10] = keyIndexBytes[2];
            startByte[11] = keyIndexBytes[3];
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < startByte.length; i++) {
            data[i] = startByte[i];
        }

        for (int j = 0; j < encryptContent.length; j++) {
            data[13 + j] = encryptContent[j];
        }

        byte[] crcByte = new byte[data.length - 4];
        System.arraycopy(data, 1, crcByte, 0, crcByte.length);

        byte[] crc = CRC16.calcCrc16(crcByte);
        data[data.length - 3] = crc[2];
        data[data.length - 2] = crc[3];
        data[data.length - 1] = (byte) 0xAE;
        LogUtil.d("data-" + HexUtil.byteToString(data));

        byte[] dataTemp = new byte[data.length - 2];
        System.arraycopy(data, 1, dataTemp, 0, dataTemp.length);
        LogUtil.d("dataTemp1-" + HexUtil.byteToString(dataTemp));

        String encryptContentStr = HexUtil.byteToString(dataTemp).replaceAll("AA", "AA 0A");
        String encryptContentStr1 = encryptContentStr.replaceAll("AE", "AA 0E");
        String encryptContentStr2 = encryptContentStr1.replaceAll("A5", "AA 05").replaceAll(" ", "");
        dataTemp = HexUtil.HexString2Bytes(encryptContentStr2);

        LogUtil.d("dataTemp2-" + HexUtil.byteToString(dataTemp));
        byte[] finalData = new byte[dataTemp.length + 2];
        System.arraycopy(dataTemp, 0, finalData, 1, dataTemp.length);

        finalData[0] = (byte) 0xa5;
        finalData[finalData.length - 1] = (byte) 0xaE;

        LogUtil.d(HexUtil.byteToString(finalData));
        return data;
    }

    public static int isSuccess(String keyIndex, String aes128key, byte[] data) {
// A5 FF 40 11 00 00 00 01 FF FF FF E0 80 [E1 89 2F 9C A9 58 DB 47 AA 05 63 6D EE 0C 33 34 42] 57 DD AE

        byte[] aes128keyByte = aes128key.getBytes();
        try {
            byte[] dataTem = new byte[data.length - 2];
            System.arraycopy(data, 1, dataTem, 0, dataTem.length);
            LogUtil.d(HexUtil.byteToString(dataTem));

            String encryptContentStr = HexUtil.byteToString(dataTem);
            String encryptContentStr1 = encryptContentStr.replaceAll("AA 0A", "AA");
            String encryptContentStr2 = encryptContentStr1.replaceAll("AA 0E", "AE");
            String encryptContentStr3 = encryptContentStr2.replaceAll("AA 05", "A5").replaceAll(" ", "");

            byte[] dataTemp = HexUtil.HexString2Bytes(encryptContentStr3);

            LogUtil.d(HexUtil.byteToString(dataTemp));
//
            int keyIndexInt = Integer.parseInt(keyIndex);
            byte[] keyIndexBytes = HexUtil.intToBytes(keyIndexInt);
            if ((keyIndexBytes[0] == dataTemp[3]) && (keyIndexBytes[1] == dataTemp[4])
                    && (keyIndexBytes[2] == dataTemp[5]) && (keyIndexBytes[3] == dataTemp[6])) {

                byte[] decryptContent = new byte[dataTemp.length - 14];
                System.arraycopy(dataTemp, 12, decryptContent, 0, decryptContent.length);
                LogUtil.d(HexUtil.byteToString(decryptContent));

                byte[] decryptData = AesUtilGate.decrypt(decryptContent, aes128keyByte);
                LogUtil.d(HexUtil.byteToString(decryptData));
                if (decryptData.length == 8 && decryptData[6] == 0) {
                    return 1;
                } else {
                    return 2;
                }
            } else {//
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
