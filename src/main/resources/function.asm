#include stdlib.asm

int left = 10
int right = 20
int zero = 0

# AND with variables
assert(0 == (zero && zero))
assert(0 == (left && zero))
assert(0 == (zero && right))
assert(right == (right && right))

# AND with constants
assert(0 == (0 && 0))
assert(0 == (0 && 10))
assert(0 == (10 && 0))
assert(20 == (10 && 20))

# AND with mixed
assert(0 == (left && 0))
assert(2 == (left && 2))
assert(0 == (0 && right))
assert(right == (2 && right))

# OR with variables
assert(0 == (zero || zero))
assert(left == (left || zero))
assert(right == (zero || right))
assert(left == (left || right))

# OR with constants
assert(0 == (0 || 0))
assert(10 == (0 || 10))
assert(10 == (10 || 0))
assert(10 == (10 || 20))

# OR with mixed
assert(left == (left || 0))
assert(left == (left || 2))
assert(right == (0 || right))
assert(2 == (2 || right))

# NOT
assert(0 == !5)
assert(0 == !-5)
assert(1 == !0)
assert(0 == !left)
assert(1 == !zero)

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

