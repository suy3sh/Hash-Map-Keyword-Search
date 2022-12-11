public class HashTableBuilder {
    Element[] hashTable;
    int tableSize;

    HashTableBuilder(int numKeyWords){
        tableSize = nextPrime(numKeyWords * 2);
        hashTable = new Element[tableSize];
    }

    private Element createNewElement(String keyword, Article recordToAdd) {
        return new Element(keyword, recordToAdd);
    }

    void insert(String keyword, FileData fd){
        Article recordToAdd = new Article(fd.id, fd.title, fd.author, null);
        int index = find(keyword);
        if (index == -1)
            insert(keyword, recordToAdd);
        else{
            recordToAdd.next = hashTable[index].head;
            hashTable[index].head = recordToAdd;
        }
    }

    private void insert(String keyword, Article recordToAdd) {
        int key = convertStringToInt(keyword);
        int index = key % tableSize;
        int probe = 1;
        while (hashTable[index] != null){
            System.out.printf("%s conflicts with %s at index: %d\n", keyword, hashTable[index].keyword, index);
            index = getNextQuadProbIndex(key, probe++);
        }
        hashTable[index] = createNewElement(keyword,recordToAdd);
    }  

    int find(String keyword){
        int key = convertStringToInt(keyword);
        int index = key % tableSize;
        int probe = 1;
        while (hashTable[index] != null){
            if (hashTable[index].keyword.compareTo(keyword) == 0)
                return index;
            index = getNextQuadProbIndex(key, probe++);
        }
        return -1;
    }

    private int getNextQuadProbIndex(int key, int probe){
        return ((key % tableSize) + (int) Math.pow(probe,2)) % tableSize;
    }

    private int convertStringToInt(String keyword){
        int sum = 0;
        for (int i = 0; i < keyword.length(); i++)
            sum += (int) keyword.charAt(i);
        return sum;
    }

    private int nextPrime(int num){
        num++;
        for (int i = 2; i < num; i++){
            if (num % i == 0){
                num ++;
                i = 2;
            } else {
                continue;
            }
        }
        return num;
    }

    public void print(){
        int printWrap = 0;
        System.out.print("\n\n\n");
        for (int index = 0; index < tableSize; index++){
            if (hashTable[index] != null){
                System.out.printf("index [%d]: %s (E ---> int): %d\n", index, 
                                        hashTable[index].keyword, 
                                        convertStringToInt(hashTable[index].keyword));
                Article rec = hashTable[index].head;
                System.out.print("\t\t");
                while (rec != null){
                    if (++printWrap % 3 == 0){
                        System.out.println();
                        System.out.print("\t\t");
                    }
                    System.out.printf("%d|%s|%s ---> ", rec.id, rec.author, rec.title);
                    rec = rec.next;
                }
                System.out.println("null\n");
                printWrap = 0;
            }
        }
    }
}
