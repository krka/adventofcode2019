int s = 0
int i = 0
int ch = 0

func outputString(s)
  i = 0
loop:
  ch = s[i]
  if ! ch jump finish
  output(ch)
  i = i + 1
  jump loop
finish:
  return
endfunc
