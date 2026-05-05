#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>

typedef struct Node {
    int key;
    int hashKey;
    int value;
    struct Node* next;
} Node;

Node* newNode(int key, int hashKey, int value) {
    Node *new = malloc(sizeof(Node));

    new->hashKey = hashKey;
    new->key = key;
    new->value = value;
    new->next = NULL;

    return new;
}

Node** newHashMap(int capacity) {
    Node **map = calloc(capacity, sizeof(Node *));

    return map;
}

int hash(int key, int capacity) {
    if (key < 0) key = -key;
    return key % capacity;
}

void insert(Node **hashMap, int key, Node *new) {
    if (hashMap[key] == NULL) {
        hashMap[key] = new;
        return ;
    }

    new->next = hashMap[key];
    hashMap[key] = new;
}

Node* get(Node **hashMap, int hashKey, int key) {
    if (hashMap[hashKey] == NULL) return NULL;

    Node *head = hashMap[hashKey];
    while (head) {
        if (head->key == key) return head;
        head = head->next;
    }

    return NULL;
}

bool containsNearbyDuplicate(int* nums, int numsSize, int k) {
 
    int capacity = 10000;
    Node **map = newHashMap(capacity);

    for(int i = 0; i < numsSize; i++) {

        int key = hash(nums[i], capacity);
        Node* oldIndex = get(map, key, nums[i]);
        if (oldIndex != NULL && i - oldIndex->value <= k) return true;
        else if (oldIndex != NULL) oldIndex->value = i;
        else insert(map, key, newNode(nums[i], key, i));
    }

    return false;
}

int main() {

    int nums[] = {1, 2, 3, 1};
    int k = 3;

    printf("Contains Nearby Duplicate: %s\n", containsNearbyDuplicate(nums, 4, k) ? "true" : "false"); // true

    int nums2[] = {1, 0, 1, 1};
    k = 1;

    printf("Contains Nearby Duplicate: %s\n", containsNearbyDuplicate(nums2, 4, k) ? "true" : "false"); // true

    int nums3[] = {1, 2, 3, 1, 2, 3};
    k = 2;

    printf("Contains Nearby Duplicate: %s\n", containsNearbyDuplicate(nums3, 6, k) ? "true" : "false"); // false

    return 0;
}