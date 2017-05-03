import random

if __name__ == '__main__':

    with open('gene.txt','w') as f:
        for i in range(30):
            for j in range(30):
                f.write(str(random.randint(0,9)))
                f.write(' ')
            f.write('\n')