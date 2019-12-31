#include outputstring.asm
#include division.asm

array[20] tempnumber
int i = 0
int tempn = 0
int remainder = 0

func outputNumber n
i = 0
tempn = n

label populate
call div tempn 10 : tempn remainder
remainder = remainder + 48
setarray tempnumber i remainder
i = i + 1
if tempn jump populate

label output
i = i + -1

getarray tempnumber i tempn
output tempn
if not i jump finish
jump output

label finish
return

endfunc

