func assert(condition)
  if ! condition then
    throw()
  end
end

func strlen(s)
  int i = 0
  while 1 do
    if s[i] == 0 then
       return i
    end
    i = i + 1
  end
  # unreachable
  throw()
end

func strcmp(s1, s2)
  for i = 0, 1 do
    int c1 = s1[i]
    int c2 = s2[i]
    int diff = c1 - c2
    if diff || c1 == 0 || c2 == 0 then
      return diff
    end
  end
  # unreachable
  throw()
end

func memcpy(n, src, dest)
  while n > 0 do
    n = n - 1
    dest[n] = src[n]
  end
end

int peek = -1

func read()
  if peek != -1 then
    int x = peek
    peek = -1
    return peek
  end
  return input()
end

func peek()
  if peek == -1 then
    peek = input()
  end
  return peek
end

func skip_non_int()
  while 1 do
    int x = peek()
    if x >= '0' && x <= '9' then
      return
    end
    read()
  end
  return
end

func skip_non_chars()
  while 1 do
    int x = peek()
    if x >= 'a' && x <= 'z' then
      return
    end
    read()
  end
  return
end

func read_int()
  int res = 0

  while 1 do
    int x = peek()
    if x < '0' || x > '9' then
      return res
    else
      read()
      res = res * 10 + (x - '0')
    end
  end
  return -1
end

func read_chars(buf)
  int i = 0

  while 1 do
    int x = peek()
    if x < 'a' || x > 'z' then
      buf[i] = 0
      return
    else
      read()
      buf[i] = x
      i = i + 1
    end
  end
  buf[i] = 0
  return
end

func skip_until(c)
  while 1 do
    int x = peek()
    if x != c then
      return
    else
      read()
    end
  end
end

func read_until(buf, c)
  int i = 0

  while 1 do
    int x = peek()
    if x == c then
      buf[i] = 0
      return
    else
      read()
      buf[i] = x
      i = i + 1
    end
  end
  buf[i] = 0
  return
end
