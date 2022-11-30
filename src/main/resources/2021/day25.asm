array[200*200] grid
int cols = 0
int rows = 1

while 1 do
  int v = input()
  if v == 10 then
    break
  end
  grid[cols] = v
  cols = cols + 1
end

int offset = 0
int done = 0
while 1 do
  offset = offset + cols
  for c = 0, c < cols do
    int v = input()
    if v == 10 then
      if c == 0 then
        done = 1
        break
      else
        throw()
      end
    end
    grid[offset + c] = v
  end

  if done then
    break
  end

  # discard newline
  int v = input()
  if v != 10 then
    throw()
  end

  rows = rows + 1
end

int step = 1
while 1 do
  int done = 1
  for r = 0, r < rows do
    offset = r * cols
    int prev = offset + cols - 1
    for c = 0, c < cols do
      int cur = offset + c
      if grid[cur] == '.' then
        if grid[prev] == '>' then
          grid[prev] = '1'
          done = 0
        end
      end
      prev = cur
    end
  end
  int prevRow = rows - 1
  for r = 0, r < rows do
    offset = r * cols
    for c = 0, c < cols do
      int cur = offset + c
      if grid[cur] == '.' then
        int x = prevRow * cols + c
        if grid[x] == 'v' then
          grid[x] = '2'
          done = 0
        end
      end
    end
    prevRow = r
  end
  if done then
    break
  end
  for r = 0, r < rows do
    int offset = r * cols
    for c = 0, c < cols do
      int i = offset + c
      int v = grid[i]
      if v == '1' then
        grid[i] = '.'
        int i2 = i + 1
        if c == cols - 1 then
          i2 = i2 - cols
        end
        grid[i2] = '>'
      elseif v == '2' then
        grid[i] = '.'
        int i2 = i + cols
        if r == rows - 1 then
          i2 = c
        end
        grid[i2] = 'v'
      end
    end
  end
  step = step + 1
end

output(step)
