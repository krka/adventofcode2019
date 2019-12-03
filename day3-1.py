import fileinput

grid = dict()

maxdist = 0

def put(x, y, n):
  global maxdist, grid
  dist = abs(x) + abs(y)
  if dist > maxdist: maxdist = dist
  coord = str(x) + "," + str(y)
  prev = grid.setdefault(coord, 0)
  grid[coord] |= n

def putall(cmds, n):
  x, y = 0, 0
  put(x, y, n)
  for cmd in cmds.split(","):
    c = cmd[0]
    num = int(cmd[1:])
    for i in range(num):
      if c == 'R': x += 1
      if c == 'L': x -= 1
      if c == 'U': y += 1
      if c == 'D': y -= 1
      put(x, y, n)

  
lines = [line.strip() for line in fileinput.input() if len(line.strip()) > 0]

putall(lines[0], 1)
putall(lines[1], 2)

def get(x, y):
  global grid
  return grid.get(str(x) + "," + str(y), 0)

def has3(d, x, y, dx, dy):
  for i in range(0, d + 1):
    if get(x + i*dx, y + i*dy) == 3:
      return True
  return False

def solve():
  global maxdist
  for dist in range(1, maxdist):
    if has3(dist, 0, dist, 1, -1): return dist
    if has3(dist, 0, -dist, -1, 1): return dist
    if has3(dist, dist, 0, -1, 1): return dist
    if has3(dist, -dist, 0, 1, 1): return dist

print(solve())
  
