package com.tddworks.lemonsqueezy.api.user.response

import com.tddworks.lemonsqueezy.api.user.data.UserAttributes


/**
 * {
 *   "type": "users",
 *   "id": "1",
 *   "attributes": {
 *     "name": "Darlene Daugherty",
 *     "email": "gernser@yahoo.com",
 *     "color": "#898FA9",
 *     "avatar_url": "https://www.gravatar.com/avatar/1ace5b3965c59dbcd1db79d85da75048?d=blank",
 *     "has_custom_avatar": false,
 *     "createdAt": "2021-05-24T14:08:31.000000Z",
 *     "updatedAt": "2021-08-26T13:24:54.000000Z"
 *   }
 * }
 */
data class UserResponse(
    val type: String,
    val id: String,
    val attributes: UserAttributes,
)