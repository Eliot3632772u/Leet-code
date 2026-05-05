#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct Node {
    int value;
    struct Node* next;
} Node;

Node* newNode(int value) {

    Node *new = malloc(sizeof(Node));

    new->value = value;
    new->next = NULL;

    return new;
}

Node** newHashSet(int capacity) {

    Node **new = calloc(capacity, sizeof(Node*));
    return new;
}

int hash(int key, int capacity) {

    if(key < 0) key = -key;

    return key % capacity;
}

void insert(Node** set, Node* new, int key) {

    if (set[key] == NULL) {
        set[key] = new;
        return;
    }

    new->next = set[key];
    set[key] = new;

}

Node* get(Node** set, int key, int value) {

    if (set[key] == NULL) return NULL;

    Node *head = set[key];
    while (head) {
        if (head->value == value) return head;
        head = head->next;
    }

    return NULL;
}

bool containsDuplicate(int* nums, int numsSize) {
    
    int capacity = 10000;
    Node **hashSet = newHashSet(capacity);

    for(int i = 0; i < numsSize; i++) {
        int key = hash(nums[i], capacity);
        Node *value = get(hashSet, key, nums[i]);
        if (value != NULL) return true;
        insert(hashSet, newNode(nums[i]), key);
    }

    return false;
}

int main() {

    int nums[] = {1, 2, 3, 1};
    int numsSize = sizeof(nums) / sizeof(nums[0]);

    bool result = containsDuplicate(nums, numsSize);
    printf("%s\n", result ? "true" : "false");

    return 0;
}