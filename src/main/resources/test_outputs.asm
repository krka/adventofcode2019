#include outputstring.asm
#include stdlib.asm

string helloworld = "Hello World!"

outputString(helloworld)
output(10)

func teststring()
  string s = "Goodbye world"
  return s
end

outputString(teststring())
output(10)

array[strlen(helloworld) + 1] copy
memcpy(strlen(helloworld) + 1, helloworld, copy)

outputString(copy)
output(10)
