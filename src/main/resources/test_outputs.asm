#include outputstring.asm

string helloworld = "Hello World!"

outputString(helloworld)
output(10)

func teststring()
  string s = "Goodbye world"
  return s
endfunc

outputString(teststring())
output(10)
