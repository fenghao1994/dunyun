package net.dunyun.framework.android.mainapp.util;

/**
 * @author chenzp
 * @date 2016/5/30
 * @Copyright:重庆平软科技有限公司
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class FileToCRCUtil {

    public static byte[] getCRC32(byte[] bytes){
        int[] table = {
                0X00000000,  0X04c11db7,  0X09823b6e,  0X0d4326d9,  0X130476dc,  0X17c56b6b,  0X1a864db2,  0X1e475005,
                0X2608edb8,  0X22c9f00f,  0X2f8ad6d6,  0X2b4bcb61,  0X350c9b64,  0X31cd86d3,  0X3c8ea00a,  0X384fbdbd,
                0X4c11db70,  0X48d0c6c7,  0X4593e01e,  0X4152fda9,  0X5f15adac,  0X5bd4b01b,  0X569796c2,  0X52568b75,
                0X6a1936c8,  0X6ed82b7f,  0X639b0da6,  0X675a1011,  0X791d4014,  0X7ddc5da3,  0X709f7b7a,  0X745e66cd,
                0X9823b6e0,  0X9ce2ab57,  0X91a18d8e,  0X95609039,  0X8b27c03c,  0X8fe6dd8b,  0X82a5fb52,  0X8664e6e5,
                0Xbe2b5b58,  0Xbaea46ef,  0Xb7a96036,  0Xb3687d81,  0Xad2f2d84,  0Xa9ee3033,  0Xa4ad16ea,  0Xa06c0b5d,
                0Xd4326d90,  0Xd0f37027,  0Xddb056fe,  0Xd9714b49,  0Xc7361b4c,  0Xc3f706fb,  0Xceb42022,  0Xca753d95,
                0Xf23a8028,  0Xf6fb9d9f,  0Xfbb8bb46,  0Xff79a6f1,  0Xe13ef6f4,  0Xe5ffeb43,  0Xe8bccd9a,  0Xec7dd02d,
                0X34867077,  0X30476dc0,  0X3d044b19,  0X39c556ae,  0X278206ab,  0X23431b1c,  0X2e003dc5,  0X2ac12072,
                0X128e9dcf,  0X164f8078,  0X1b0ca6a1,  0X1fcdbb16,  0X018aeb13,  0X054bf6a4,  0X0808d07d,  0X0cc9cdca,
                0X7897ab07,  0X7c56b6b0,  0X71159069,  0X75d48dde,  0X6b93dddb,  0X6f52c06c,  0X6211e6b5,  0X66d0fb02,
                0X5e9f46bf,  0X5a5e5b08,  0X571d7dd1,  0X53dc6066,  0X4d9b3063,  0X495a2dd4,  0X44190b0d,  0X40d816ba,
                0Xaca5c697,  0Xa864db20,  0Xa527fdf9,  0Xa1e6e04e,  0Xbfa1b04b,  0Xbb60adfc,  0Xb6238b25,  0Xb2e29692,
                0X8aad2b2f,  0X8e6c3698,  0X832f1041,  0X87ee0df6,  0X99a95df3,  0X9d684044,  0X902b669d,  0X94ea7b2a,
                0Xe0b41de7,  0Xe4750050,  0Xe9362689,  0Xedf73b3e,  0Xf3b06b3b,  0Xf771768c,  0Xfa325055,  0Xfef34de2,
                0Xc6bcf05f,  0Xc27dede8,  0Xcf3ecb31,  0Xcbffd686,  0Xd5b88683,  0Xd1799b34,  0Xdc3abded,  0Xd8fba05a,
                0X690ce0ee,  0X6dcdfd59,  0X608edb80,  0X644fc637,  0X7a089632,  0X7ec98b85,  0X738aad5c,  0X774bb0eb,
                0X4f040d56,  0X4bc510e1,  0X46863638,  0X42472b8f,  0X5c007b8a,  0X58c1663d,  0X558240e4,  0X51435d53,
                0X251d3b9e,  0X21dc2629,  0X2c9f00f0,  0X285e1d47,  0X36194d42,  0X32d850f5,  0X3f9b762c,  0X3b5a6b9b,
                0X0315d626,  0X07d4cb91,  0X0a97ed48,  0X0e56f0ff,  0X1011a0fa,  0X14d0bd4d,  0X19939b94,  0X1d528623,
                0Xf12f560e,  0Xf5ee4bb9,  0Xf8ad6d60,  0Xfc6c70d7,  0Xe22b20d2,  0Xe6ea3d65,  0Xeba91bbc,  0Xef68060b,
                0Xd727bbb6,  0Xd3e6a601,  0Xdea580d8,  0Xda649d6f,  0Xc423cd6a,  0Xc0e2d0dd,  0Xcda1f604,  0Xc960ebb3,
                0Xbd3e8d7e,  0Xb9ff90c9,  0Xb4bcb610,  0Xb07daba7,  0Xae3afba2,  0Xaafbe615,  0Xa7b8c0cc,  0Xa379dd7b,
                0X9b3660c6,  0X9ff77d71,  0X92b45ba8,  0X9675461f,  0X8832161a,  0X8cf30bad,  0X81b02d74,  0X857130c3,
                0X5d8a9099,  0X594b8d2e,  0X5408abf7,  0X50c9b640,  0X4e8ee645,  0X4a4ffbf2,  0X470cdd2b,  0X43cdc09c,
                0X7b827d21,  0X7f436096,  0X7200464f,  0X76c15bf8,  0X68860bfd,  0X6c47164a,  0X61043093,  0X65c52d24,
                0X119b4be9,  0X155a565e,  0X18197087,  0X1cd86d30,  0X029f3d35,  0X065e2082,  0X0b1d065b,  0X0fdc1bec,
                0X3793a651,  0X3352bbe6,  0X3e119d3f,  0X3ad08088,  0X2497d08d,  0X2056cd3a,  0X2d15ebe3,  0X29d4f654,
                0Xc5a92679,  0Xc1683bce,  0Xcc2b1d17,  0Xc8ea00a0,  0Xd6ad50a5,  0Xd26c4d12,  0Xdf2f6bcb,  0Xdbee767c,
                0Xe3a1cbc1,  0Xe760d676,  0Xea23f0af,  0Xeee2ed18,  0Xf0a5bd1d,  0Xf464a0aa,  0Xf9278673,  0Xfde69bc4,
                0X89b8fd09,  0X8d79e0be,  0X803ac667,  0X84fbdbd0,  0X9abc8bd5,  0X9e7d9662,  0X933eb0bb,  0X97ffad0c,
                0Xafb010b1,  0Xab710d06,  0Xa6322bdf,  0Xa2f33668,  0Xbcb4666d,  0Xb8757bda,  0Xb5365d03,  0Xb1f740b4
        };
        int crc = 0xffffffff;
        int tabitem = 0;
        for (byte b : bytes) {
            tabitem = (crc >> 24) ^ (b);
            tabitem = tabitem & 0xff;
            crc = (crc << 8) ^ table[tabitem];
        }

        byte[] a = new byte[4];
        a[3] = (byte) (0xff & crc);
        a[2] = (byte) ((0xff00 & crc) >> 8);
        a[1] = (byte) ((0xff0000 & crc) >> 16);
        a[0] = (byte) ((0xff000000 & crc) >> 24);
        return a;
    }

}
