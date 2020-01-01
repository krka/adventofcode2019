#include outputstring.asm
#include division.asm

array[20] tempnumber
int i = 0
int tempn = 0
int remainder = 0

func outputNumber(n)
  i = 0
  tempn = n

populate:
  tempn, remainder = div(tempn, 10)
  remainder = remainder + 48
  tempnumber[i] = remainder
  i = i + 1
  if tempn jump populate

output:
  i = i + -1

  tempn = tempnumber[i]
  output(tempn)
  if not i jump finish
  jump output

finish:
  return

endfunc

