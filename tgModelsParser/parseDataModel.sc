println("parsing fields and types for tg model html present in input.txt")

import $ivy.`com.lihaoyi::os-lib:0.7.2`
import $ivy.`com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20200713.1`
object utils {
    def pln(x:Any) = println(x)
}; import utils._

val wd = os.pwd
val inputF = wd / "tgModelsParser" / "input.txt"
val outputF = wd / "tgModelsParser" / "output.txt"
val inputText: String = os.read(inputF)

val inputList = inputText.split("\n").tail.init.filterNot(s => (s == "<tr>" || s == "</tr>")).toList

val groupedList = inputList.grouped(3).toList
val groupedAsTuples = groupedList.map(ls => Tuple3(ls(0),ls(1),ls(2)))

def transformTriplet(t3: Tuple3[String,String,String]) = {
  identity(t3)
}
import org.owasp.html._
val policy: PolicyFactory = new HtmlPolicyBuilder()
    .allowElements("p")
    .toFactory();

def transformListTriplets(l3: List[String]) = {
  l3.
    map(l3 => l3.drop(4).dropRight(5)).
    map(s => if(s == "Integer") "Int" else s).
    map(s => policy.sanitize(s)).
    map(s => if (s.startsWith("Array of ")) {s.replaceAllLiterally("Array of ","Array[")+"]"} else s)
    
}


def step2(step1: List[String]) = if (step1(2).startsWith("Optional")) List(step1(0), s"Option[${step1(1)}]", step1(2)) else List(step1(0), step1(1), step1(2))

val outputText =
  groupedList.
  map(l3 => transformListTriplets(l3)).
  map(l3 => step2(l3)).
  map(l3 => List(s"""/* ${l3(2).replaceAllLiterally("&#39;","'")} */""", List( l3(0),l3(1) ).mkString(": ")+", " ).mkString("\n")).
  mkString("\n\n")

os.write.over(outputF, outputText)
println("written parsed fields and types to output.txt")
