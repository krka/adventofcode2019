int tmp = 0

func foo(a, b)
  tmp = a + b
  return tmp
endfunc

func bar()
  return 1234
endfunc

int x = 0
int y = 0
int z = 0

x = 100 + 0
y = 23 + 0

z = foo(x, y)
output(z)

z = bar()
output(z)

halt
