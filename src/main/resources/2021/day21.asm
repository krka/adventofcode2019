int KEY_SPACE = 2 * 10 * 10 * 22 * 22
array[KEY_SPACE + 2] cache
cache[KEY_SPACE + 1] = 1

array[7] diecounts
for d1 = 0, d1 < 3 do
  for d2 = 0, d2 < 3 do
    for d3 = 0, d3 < 3 do
      int index = d1 + d2 + d3
      diecounts[index] = diecounts[index] + 1
    end
  end
end

func solve(p1, p2, s1, s2)
  if s2 >= 21 then
    return KEY_SPACE
  end
  
  int key = p1

  key = key * 10
  key = key + p2

  key = key * 22
  key = key + s1

  key = key * 22
  key = key + s2

  key = key * 2

  int key2 = key + 1
  if cache[key] + cache[key2] then
    return key
  end
  int sumA = 0
  int sumB = 0
  int s1plus = s1 + 1
  for i = 0, i < 7 do
    int counts = diecounts[i]
    int newP1 = p1 + i + 3
    if newP1 >= 10 then
      newP1 = newP1 - 10
    end
    int newS1 = s1plus + newP1
    int index = solve(p2, newP1, s2, newS1)
    sumA = sumA + counts * cache[index]
    sumB = sumB + counts * cache[index + 1]
  end
  cache[key] = sumB
  cache[key2] = sumA
  return key
end


int p1 = input()
int p2 = input()

int index = solve(p1 - 1, p2 - 1, 0, 0)
int ans1 = cache[index]
int ans2 = cache[index + 1]
if ans1 > ans2 then
  output(ans1)
else
  output(ans2)
end


