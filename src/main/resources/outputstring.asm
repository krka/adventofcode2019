int s = 0
int i = 0
int ch = 0

func outputString sptr
i = 0
loop:
getarrayptr sptr i ch
if not ch jump finish
output(ch)
i = i + 1
jump loop
finish:
return
endfunc
