import os

print "-=vidyascape mac scanner=-"
print ""

class Person:
    def __init__(self,mac,name):
        self.mac = mac
        self.names = []
        self.names.append(name)
        self.numberofaccounts = 1
    def incaccounts(self):
        self.numberofaccounts += 1
    def addname(self,name):
        self.names.append(name)
    def getnames(self):
        return self.names
    def getmac(self):
        return self.mac
    def getnum(self):
        return self.numberofaccounts

list = []

path = 'C:\\Users\\Travis\\Desktop\\rsps\\backups\\aug 10'

for filename in os.listdir(path):
    if filename.endswith(".txt"):
        f = open(path + "\\" + filename, 'r')
        for line in f:
            if "MAC" in line:
                found = False
                for person in list:
                    if person.mac == line[6:-1]:
                        found = True
                        person.incaccounts()
                        person.addname(os.path.split(filename))
                if found == False:
                    new = Person(line[6:-1],os.path.split(filename))
                    list.append(new)
list.sort(cmp = lambda y, x: cmp(x.numberofaccounts, y.numberofaccounts))
k = open('out.txt', 'w')
for person in list:
    k.write('\n')
    k.write(person.getmac() + " has " + str(person.getnum()) + " accounts" + '\n')
    s = ""
    for name in person.getnames():
        s += name[1] + ' '
    k.write(s)

k.close()
