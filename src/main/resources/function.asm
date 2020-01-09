#include stdlib.asm

int left = 10
int right = 20
int zero = 0
assert(0 == (zero && zero))
assert(0 == (left && zero))
assert(0 == (zero && right))
assert(right == (right && right))

assert(0 == (0 && 0))
assert(0 == (0 && 1))
assert(0 == (1 && 0))
assert(1 == (1 && 1))
assert(0 == (left && 0))
assert(0 == (0 && right))
assert(right == (1 && right))

assert(0 == (zero || zero))
assert(left == (left || zero))
assert(right == (zero || right))
assert(left == (left || right))

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
  int r = 0
  r = n * rec(n - 1)
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

