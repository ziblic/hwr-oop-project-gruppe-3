package hwr.oop

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody

class Arguments(parser: ArgParser) {
    val verbose by parser.flagging("-v", "--verbose", help = "enable verbose mode")

    val name by parser.storing("-N", "--name", help = "name of the widget").default("John Doe")

    val size by parser.storing("-s", "--size", help = "size of the plumbus") { toInt() }

    val sources by
            parser.positionalList("SOURCE", help = "source filename", sizeRange = 1..Int.MAX_VALUE)

    val destination by parser.positional("DEST", help = "destination filename")
}

fun main(args: Array<String>) = mainBody {
    val parsedArgs = ArgParser(args).parseInto(::Arguments)
    parsedArgs.run {
        println(
                """
                verbose =     $verbose
                name =        $name
                size =        $size
                sources =     $sources
                destination = $destination""".trimIndent()
        )
    }
}
