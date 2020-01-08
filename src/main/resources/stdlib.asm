func assert(condition)
  if condition jump ok
  throw()
ok:
endfunc

func strlen(s)
  int i = 0
loop:
  if s[i] == 0 jump doreturn
  i = i + 1
  jump loop
doreturn:
  return i
endfunc

func strcmp(s1, s2)
  int i = 0
  int c1 = 0
  int c2 = 0
  int diff = 0
loop:
  c1 = s1[i]
  c2 = s2[i]
  diff = c1 - c2
  if diff jump doreturn
  if c1 == 0 jump doreturn
  if c2 == 0 jump doreturn
  i = i + 1
  jump loop

doreturn:
  return diff
endfunc
