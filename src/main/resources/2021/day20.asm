#include stdlib.asm

array[513] algo

# Initial grid size is 100 * 100, but it grows 2 for each step, so eventual size is 2 * 50 + 100 = 200
# 200 * 200 = 40000
array[41000] grid1
array[41000] grid2

read_until(algo, 10)
for i = 0, i < 512 do
  algo[i] = (algo[i] == '#')
end


int even = 0
if algo[0] == 1 then
  if algo[511] == 0 then
    even = 1
  else
    throw()
  end
end

# skip the newlines
read()
read()

int srcsize = 0
while 1 do
  int c = read()
  if c == 10 then
    break
  end
  grid1[srcsize] = (c == '#')
  srcsize = srcsize + 1
end

for row = 1, row < srcsize do
  for col = 0, col < srcsize do
    int c = read()
    grid1[row * srcsize + col] = (c == '#')
  end
  # skip the newline
  read()
end

# Global variables for performance reasons
int src = grid1
int dest = grid2
int destsize = 0
int default = 0

func count()
  int c = 0
  int i = srcsize * srcsize
  while i > 0 do
    i = i - 1
    c = c + src[i]
  end
  return c
end

func set_unsafe(row, col)
  int index = 0
  for r = -2, r <= 0 do
    int r2 = row + r
    int r_ok = r2 >= 0 && r2 < srcsize
    r2 = r2 * srcsize
    for c = -2, c <= 0 do
      int val = default
      int c2 = col + c
      if r_ok && c2 >= 0 && c2 < srcsize then
        val = src[r2 + c2]
      end
      index = 2 * index + val
    end
  end
  dest[row * destsize + col] = algo[index]
end

func step(default_value)
  default = default_value
  destsize = srcsize + 2

  int last = destsize - 1
  for row = 0, row < destsize do
    set_unsafe(row, 0)
    set_unsafe(row, 1)
    set_unsafe(row, srcsize)
    set_unsafe(row, last)
  end
  for col = 0, col < destsize do
    set_unsafe(0, col)
    set_unsafe(1, col)
    set_unsafe(srcsize, col)
    set_unsafe(last, col)
  end

  last = srcsize - 2
  int dest_offset = destsize + 2
  for row = 0, row < last do
    int offset = row * srcsize
    dest_offset = dest_offset + destsize
    for col = 0, col < last do
      int offset2 = offset + col

      int index = src[offset2]
      index = 2 * index + (src[offset2 + 1])
      index = 2 * index + (src[offset2 + 2])
      offset2 = offset2 + srcsize
      index = 2 * index + (src[offset2])
      index = 2 * index + (src[offset2 + 1])
      index = 2 * index + (src[offset2 + 2])
      offset2 = offset2 + srcsize
      index = 2 * index + (src[offset2])
      index = 2 * index + (src[offset2 + 1])
      index = 2 * index + (src[offset2 + 2])

      dest[dest_offset + col] = algo[index]
    end
  end

  srcsize = destsize

  int tmp = src
  src = dest
  dest = tmp

end

step(0)
step(even)

int ans = count()
output(ans)

for i = 0, i < 24 do
  step(0)
  step(even)
end

ans = count()
output(ans)
