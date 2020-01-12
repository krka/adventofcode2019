#include outputstring.asm
#include division.asm

array[20] tempnumber
int i = 0
int tempn = 0
int remainder = 0

func outputNumber(n)
  i = 0
  tempn = n
  while tempn
    tempn, remainder = div(tempn, 10)
    remainder = remainder + 48
    tempnumber[i] = remainder
    i = i + 1
  end

  while i
    i = i - 1
    output(tempnumber[i])
  end
end

