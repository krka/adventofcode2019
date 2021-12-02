#include outputstring.asm
#include division.asm

array[20] tempnumber
int i = 0
int tempn = 0
int remainder = 0

func outputNumber(n)
  if n < 0 then
    output('-')
    n = -n
  end
  if n == 0 then
    output('0')
    return
  end

  i = 0
  tempn = n
  while tempn do
    tempn, remainder = div(tempn, 10)
    tempnumber[i] = remainder + '0'
    i = i + 1
  end

  while i do
    i = i - 1
    output(tempnumber[i])
  end
end
