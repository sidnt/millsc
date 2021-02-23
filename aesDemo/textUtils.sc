object textUtils {
    def arb2b64(arb:Array[Byte]): String = java.util.Base64.getEncoder.encodeToString(arb)
    def b642arb(str:String): Array[Byte] = java.util.Base64.getDecoder.decode(str)
}