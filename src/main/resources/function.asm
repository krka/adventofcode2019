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

x = 100
y = 23

z = foo(x, y)
output(z)

z = bar()
output(z)

func rec(n)
  if n == 0 jump finish1
  int n2 = -1
  int r = 0
  n2 = n + n2
  r = rec(n2)
  r = r * n
  return r
finish1:
  return 1
endfunc

z = rec(5) + 0
output(z)

z = 10 + rec(6) - 10
output(z)

func implicitReturn()
endfunc

implicitReturn()

x = 100 * -x * (y + z) + 7

output(x)


