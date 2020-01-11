func outputString(s)
  int i = 0
loop:
  int ch = s[i]
  if ! ch jump finish
  output(ch)
  i = i + 1
  jump loop
finish:
  return
endfunc
