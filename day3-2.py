import fileinput

grid = dict()

def put(x, y, n, steps):
  global grid
  coord = str(x) + "," + str(y)
  (left, right) = grid.setdefault(coord, (0, 0))
  if n == 1 and (steps < left or left == 0):
    left = steps
  if n == 2 and (steps < right or right == 0):
    right = steps
  grid[coord] = (left, right)

def putall(cmds, n):
  x, y = 0, 0
  steps = 0
  put(x, y, n, steps)
  for cmd in cmds.split(","):
    c = cmd[0]
    num = int(cmd[1:])
    for i in range(num):
      steps += 1
      if c == 'R': x += 1
      if c == 'L': x -= 1
      if c == 'U': y += 1
      if c == 'D': y -= 1
      put(x, y, n, steps)

  
lines = [line.strip() for line in fileinput.input() if len(line.strip()) > 0]

putall(lines[0], 1)
putall(lines[1], 2)

def solve():
  best = 100000000
  for (left, right) in grid.values():
    if left != 0 and right != 0:
      best = min(best, left + right)
  return best

print(solve())
  
